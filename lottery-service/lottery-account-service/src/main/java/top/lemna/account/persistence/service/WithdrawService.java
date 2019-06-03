package net.zhongss.kaolabao.core.persistence.account.service;

import com.baidu.fsg.uid.UidGenerator;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import net.zhongss.kaolabao.core.base.service.BaseService;
import net.zhongss.kaolabao.core.bean.WithDrawExportBean;
import net.zhongss.kaolabao.core.enums.BillType;
import net.zhongss.kaolabao.core.enums.CommissionType;
import net.zhongss.kaolabao.core.enums.SettleType;
import net.zhongss.kaolabao.core.enums.WithdrawStatus;
import net.zhongss.kaolabao.core.enums.WithdrawStyle;
import net.zhongss.kaolabao.core.enums.WithdrawType;
import net.zhongss.kaolabao.core.exception.WithdrawException;
import net.zhongss.kaolabao.core.persistence.account.domain.AccountSettleCard;
import net.zhongss.kaolabao.core.persistence.account.domain.Withdraw;
import net.zhongss.kaolabao.core.persistence.account.repository.WithdrawRepository;
import net.zhongss.kaolabao.core.persistence.account.service.dto.WithdrawDto;
import net.zhongss.kaolabao.core.persistence.account.service.dto.WithdrawQueryDto;
import net.zhongss.kaolabao.core.persistence.agent.domain.AgentActivityRewardRecord;
import net.zhongss.kaolabao.core.persistence.agent.domain.AgentInfo;
import net.zhongss.kaolabao.core.persistence.agent.service.AgentActivityRewardRecordService;
import net.zhongss.kaolabao.core.persistence.agent.service.AgentInfoService;
import net.zhongss.kaolabao.core.utils.AmountUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class WithdrawService extends BaseService<Withdraw> {

  @SuppressWarnings("unused")
  @Autowired
  private WithdrawRepository repository;

  @Autowired
  private AccountSettleCardService accountSettleCardService;

  @Autowired
  private UidGenerator uidGenerator;

  @Autowired
  private AccountService accountService;

  @Autowired
  private AgentInfoService agentInfoService;

  @Autowired
  private AgentActivityRewardRecordService agentActivityRewardRecordService;

  @PersistenceContext
  private EntityManager em;

  private Withdraw create(Long orderNo, Long accountNo, Long amount, WithdrawType type,
      String remark,long taxes,WithdrawStatus status, WithdrawStyle style) {
    Withdraw withDraw = new Withdraw(orderNo, accountNo, amount, type, remark,taxes,status,style);
    return save(withDraw);
  }

  /**
   * 创建提现记录
   * 
   * @param accountNo 要提现的账户
   * @param amount 提现金额
   * @param type 提现渠道
   * @param remark 备注 </br>
   *        如果是提现到银行卡 请按 开户行|户名|卡号|证件号 拼写数据 如 建设银行|张三|43921234567820|10010120011010001</br>
   *        如果是提现到支付宝或微信，则在此处填写支付宝或微信账号</br>
   * @return
   */
  @Transactional
  public Withdraw place(Long accountNo, Long amount, WithdrawType type, String remark,double feeRate,
      WithdrawStatus status,WithdrawStyle style) {
    long orderNo = uidGenerator.getUID();
    // 将用户账户可用余额扣减
    long taxes = 0l;//税费
    if(feeRate > 0){
      taxes = new Double(Math.ceil(AmountUtils.mul(amount, feeRate))).longValue();//税款
      accountService.debit(accountNo, amount - taxes,orderNo, BillType.WITHDRAW_CASH, remark);
      accountService.debit(accountNo, taxes,orderNo, BillType.TAXES, remark);
    }else{
      accountService.debit(accountNo, amount,orderNo, BillType.WITHDRAW_CASH, remark);
    }
    Withdraw withdraw = create(orderNo, accountNo, amount, type, remark,taxes,status,style);
    return withdraw;
  }

  /**
   * 提现成功</br>
   * 判断提现状态是否为处理中，如果为处理中，则修改状态为成功。
   *
   * @param orderNo 提现定单号
   */
  @Transactional
  public void success(Long orderNo) {
    Withdraw withdraw = findById(orderNo);
    if (withdraw.getStatus() != WithdrawStatus.PROCESSING) {
      log.error("提现记录状态不正确，orderNo:{}", orderNo);
      throw new WithdrawException(orderNo);
    }
    withdraw.setStatus(WithdrawStatus.SUCCESS);
    save(withdraw);
  }

  /**
   * 取消提现</br>
   * 判断提现状态是否为处理中，如果为处理中，则修改状态为退款，并将提现款项返回至原账户
   * 
   * @param orderNo 提现定单号
   * @param remark 取消原因
   */
  @Transactional
  public void cancel(Long orderNo, String remark) {
    Withdraw withdraw = findById(orderNo);

    if (withdraw.getStatus() != WithdrawStatus.PROCESSING) {
      log.error("提现记录状态不正确，orderNo:{}", orderNo);
      throw new WithdrawException(orderNo);
    }
    withdraw.setStatus(WithdrawStatus.REFUND);
    withdraw.setRemark(withdraw.getRemark() + "|" + remark);
    save(withdraw);
    // 将用户账户可用余额增加，并扣减冻结余额
    accountService.credit(withdraw.getAccountId(), withdraw.getAmount(), withdraw.getId(),
        BillType.WITHDRAW_BACK, remark);

  }

  public List<Withdraw> findAll(WithdrawDto dto) {
    Specification<Withdraw> specification = getWhereClause(dto);
    return repository.findAll(specification);
  }

  private Specification<Withdraw> getWhereClause(WithdrawDto dto) {
    Specification<Withdraw> specification = new Specification<Withdraw>() {
      private static final long serialVersionUID = 1L;

      @Override
      public Predicate toPredicate(Root<Withdraw> root, CriteriaQuery<?> query,
          CriteriaBuilder cb) {
        List<Predicate> predicate = new ArrayList<>();
        if (dto.getWithdrawStatus() != null) {
          predicate.add(cb.equal(root.get("status"), dto.getWithdrawStatus()));
        }
        if (dto.getWithdrawType() != null) {
          predicate.add(cb.equal(root.get("type"), dto.getWithdrawType()));
        }
        Predicate[] pre = new Predicate[predicate.size()];
        return query.where(predicate.toArray(pre)).getRestriction();
      }
    };
    return specification;
  }

  /**
   * 体现到银行卡获取remark
   * @param agentNo 编号
   */
  public String getBankRemark(Long agentNo){
    AccountSettleCard settleCard = accountSettleCardService
        .findByAccountIdAndSettleType(agentNo, SettleType.INDIVIDUAL);
    if(settleCard == null){
      return "";
    }
    return settleCard.getBankName() + "|" + settleCard.getBankAccountName()+"|"+settleCard.getBankAccountNo()+"|" + settleCard.getIdentityNo();
}

  /**
   * 激活返现秒结 自动提现
   * @param set 秒结记录
   */
  @Transactional
  public void active(Set<AgentActivityRewardRecord> set){
    for (AgentActivityRewardRecord as : set) {
      long reward = 0L;
      if (as.getType() == CommissionType.SELF) {
        reward = as.getSelfReward();
      } else if (as.getType() == CommissionType.DIFF) {
        reward = as.getSubReward();
      }
      String remark = getBankRemark(as.getAgentNo());
      AgentInfo agentInfo = agentInfoService.findByAgentNo(as.getAgentNo());
      Withdraw withdraw = place(agentInfo.getAccountId(), reward, WithdrawType.KAXING_PROXY, remark,
          0.08, WithdrawStatus.WAIT_AUDIT,WithdrawStyle.valueOf(as.getRewardType().name()));
      as.setPaymentAmount(withdraw.getAmount() - withdraw.getTaxesAmount());
      as.setPaymentTime(withdraw.getCreateTime());
      agentActivityRewardRecordService.save(as);
    }
  }

  /**
   * 代付类型提现失败</br>
   * 判断提现状态是否为处理中，如果为处理中，则修改状态为失败
   *
   * @param orderNo 提现定单号
   * @param remark 取消原因
   */
  public void proxyCancel(long orderNo, String remark) {
    Withdraw withdraw = findById(orderNo);

    if (withdraw.getStatus() != WithdrawStatus.PROCESSING) {
      log.error("提现记录状态不正确，orderNo:{}", orderNo);
      throw new WithdrawException(orderNo);
    }
    withdraw.setStatus(WithdrawStatus.REFUND);
    withdraw.setRemark(withdraw.getRemark() + "|" + remark);
    save(withdraw);
    //此类型交易不回滚账户
  }

  public Page<Withdraw> findAll(WithdrawQueryDto dto, Pageable pageable) {
    Specification<Withdraw> specification = getWhereClause(dto);
    return repository.findAll(specification, pageable);
  }

  private Specification<Withdraw> getWhereClause(final WithdrawQueryDto dto) {
    return new Specification<Withdraw>() {
      private static final long serialVersionUID = 1L;
      @Override
      public Predicate toPredicate(Root<Withdraw> root, CriteriaQuery<?> query,
          CriteriaBuilder cb) {
        List<Predicate> predicate = new ArrayList<>();
        if(dto.getAgentNo() != null){
          predicate.add(cb.equal(root.get("accountId"), dto.getAgentNo()));
        }else if (dto.getAgentNos() != null && dto.getAgentNos().size() > 0 ) {
          CriteriaBuilder.In<Long> in = cb.in(root.get("accountId").as(Long.class));
          for (Long agentNo : dto.getAgentNos()) {
            in.value(agentNo);
          }
          predicate.add(in);
        }
        if(dto.getWithdrawType() != null){
          predicate.add(cb.equal(root.get("type"), dto.getWithdrawType()));
        }
        if(dto.getWithdrawStatus() != null){
          predicate.add(cb.equal(root.get("status"), dto.getWithdrawStatus()));
        }
        if(dto.getStyle() != null){
          predicate.add(cb.equal(root.get("style"), dto.getStyle()));
        }
        if(dto.getId() != null){
          predicate.add(cb.equal(root.get("id"), dto.getId()));
        }
        if(dto.getCreateTime() != null){
          predicate.add(cb.greaterThanOrEqualTo(root.get("createTime"), dto.getCreateTime()));
        }
        if(dto.getCreateTimeTo() != null){
          predicate.add(cb.lessThanOrEqualTo(root.get("createTime"), dto.getCreateTimeTo()));
        }
        if(dto.getCompleteTime() != null){
          predicate.add(cb.greaterThanOrEqualTo(root.get("updateTime"), dto.getCompleteTime()));
        }
        if(dto.getCompleteTimeTo() != null){
          predicate.add(cb.lessThanOrEqualTo(root.get("updateTime"), dto.getCompleteTimeTo()));
        }
        if(StringUtils.isNotBlank(dto.getAccountNo()) && StringUtils.isNotBlank(dto.getAccountName())){

          predicate.add(cb.or(cb.like(root.get("remark").as(String.class), "%" + dto.getAccountNo() + "%"),
              cb.like(root.get("remark"), dto.getAccountName())));
        }else if (StringUtils.isNotBlank(dto.getAccountNo())){
          predicate.add(cb.like(root.get("remark"), "%" + dto.getAccountNo() + "%"));
        }else if (StringUtils.isNotBlank(dto.getAccountName())){
          predicate.add(cb.like(root.get("remark"), "%" + dto.getAccountName() + "%"));
        }
        Predicate[] pre = new Predicate[predicate.size()];
        return query.where(predicate.toArray(pre)).getRestriction();
      }
    };
  }

  /**
   * 更新付款单状态
   * @param accountId 账户id
   * @param remark 备注
   */
  @Transactional
  public void updateByAccountIdAndRemark(Long accountId,String remark) {
    repository.updateByAccountIdAndRemark(accountId,remark);
  }

  /**
   * 运营后台返现付款单导入
   * @param list 导入数据
   * @param modifyStatus 修改的状态
   * @param verifyStatus 验证的状态
   */
  public List<String> dealRemitPayment(List<WithDrawExportBean> list,WithdrawType type,
      WithdrawStatus modifyStatus,WithdrawStatus verifyStatus){
    List<String> msg = new ArrayList<>();
    for(WithDrawExportBean bean: list){
      Long id = Long.parseLong(bean.getId());
      Optional<Withdraw> optional = repository.findById(id);
      if(optional.isPresent()){
        String message = checkWithdrawRemarkIsExist(optional.get(), type, modifyStatus, verifyStatus);
        if(StringUtils.isNotBlank(message)){
          msg.add(message);
        }
      }else{
        msg.add("付款单号不存在 id=" + id);
      }
    }
    return msg;
  }

  /**
   * 服务商分润付款单导入
   * @param list 导入数据
   * @param agentNo 当前服务商
   * @param type 提现渠道限制死为PROXY
   * @param modifyStatus 修改的状态
   * @param verifyStatus 验证的状态
   */
  public List<String> dealRemitPaymentByAgentNo(List<WithDrawExportBean> list,Long agentNo,WithdrawType type,
      WithdrawStatus modifyStatus,WithdrawStatus verifyStatus){
    Set<Long> accountSets = repository.findAccountIdsByAgentNo(agentNo);
    List<String> msg = new ArrayList<>();
    for(WithDrawExportBean bean: list){
      Long id = Long.parseLong(bean.getId());
      Optional<Withdraw> optional = repository.findById(id);
      if(optional.isPresent()){
        Withdraw withdraw = optional.get();
        if(accountSets.contains(withdraw.getAccountId())){
          String message = checkWithdrawRemarkIsExist(optional.get(), type, modifyStatus, verifyStatus);
          if(StringUtils.isNotBlank(message)){
            msg.add(message);
          }
        }else{
          AgentInfo agentInfo = agentInfoService.findByAccountId(withdraw.getAccountId());
          msg.add("服务商编号错误 agentNo=" + agentInfo.getAgentNo());
        }
      }else{
        msg.add("付款单号不存在 id=" + id);
      }
    }
    return msg;
  }

  /**
   * 校验是否存在结算卡 使用remark判断
   * @param withdraw 付款单信息
   * @param modifyStatus 修改的状态
   * @param verifyStatus 验证的状态
   * @return 提示信息
   */
  private String checkWithdrawRemarkIsExist(Withdraw withdraw,WithdrawType type,WithdrawStatus modifyStatus,WithdrawStatus verifyStatus){
    if(StringUtils.isNotBlank(withdraw.getRemark())){
      if(type == WithdrawType.PROXY){
        return checkWithdrawTypeIsCorrect(withdraw,type,modifyStatus,verifyStatus);
      }else if(type == WithdrawType.KAXING_PROXY){
        return checkWithdrawStatusIsCorrect(withdraw,modifyStatus,verifyStatus);
      }
    }else{
      AgentInfo agentInfo = agentInfoService.findByAccountId(withdraw.getAccountId());
      return "无结算卡信息 agentNo=" + agentInfo.getAgentNo();
    }
    return null;
  }

  /**
   * 校验付款单类型是否正确
   * @param withdraw 付款单信息
   * @param type 提现类型
   * @param modifyStatus 修改的状态
   * @param verifyStatus 验证的状态
   * @return 提示信息
   */
  private String checkWithdrawTypeIsCorrect(Withdraw withdraw,WithdrawType type,WithdrawStatus modifyStatus,WithdrawStatus verifyStatus){
    if(withdraw.getType() == type){
      return checkWithdrawStatusIsCorrect(withdraw,modifyStatus,verifyStatus);
    }else{
      return "付款单类型错误 id=" + withdraw.getId();
    }
  }

  /**
   * 校验付款单状态是否正确
   * @param withdraw 付款单信息
   * @param modifyStatus 修改的状态
   * @param verifyStatus 验证的状态
   * @return 提示信息
   */
  private String checkWithdrawStatusIsCorrect(Withdraw withdraw,WithdrawStatus modifyStatus,WithdrawStatus verifyStatus){
    if(withdraw.getStatus() == verifyStatus){
      withdraw.setStatus(modifyStatus);
      repository.save(withdraw);
    }else{
      return "付款单类型错误 id=" + withdraw.getId();
    }
    return null;
  }

  /**
   * 查询提现金额与付款金额
   */
  public Map<String,Object> findTotalAmount(WithdrawQueryDto dto){
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
    Root<Withdraw> root = criteria.from(Withdraw.class);
    Specification<Withdraw> specification = getWhereClause(dto);
    Predicate allPredicates =specification.toPredicate(root, criteria, builder);
    long amount = 0L;
    long realAmount = 0L;
    CriteriaQuery<Long> amountCriteriaQuery = criteria.select(builder.sum(root.get("amount"))).where(allPredicates);
    TypedQuery<Long> amountQuery = em.createQuery(amountCriteriaQuery);
    if (amountQuery.getSingleResult() != null) {
      amount = amountQuery.getSingleResult();
    }
    CriteriaQuery<Long> realCriteriaQuery = criteria.select(builder.sum(root.get("taxesAmount"))).where(allPredicates);
    TypedQuery<Long> realQuery = em.createQuery(realCriteriaQuery);
    if (realQuery.getSingleResult() != null) {
      Long taxesAmount = realQuery.getSingleResult();
      realAmount = amount - taxesAmount;
    }
    Map<String,Object> map = new HashMap<>();
    map.put("amount", AmountUtils.div(amount, 100));
    map.put("realAmount", AmountUtils.div(realAmount, 100));
    return map;
  }

  /**
   * 通过机构号获取账户ID集合
   * @param agencyNo 机构号
   * @return 账户id
   */
  public Set<Long> findByAgencyNo(Long agencyNo){
    Set<BigInteger> bigIntegers= repository.findByAgencyNo(agencyNo);
    Set<Long> set = new HashSet<>();
    for(BigInteger bigInteger:bigIntegers){
      set.add(bigInteger.longValue());
    }
    return set;
  }
}
