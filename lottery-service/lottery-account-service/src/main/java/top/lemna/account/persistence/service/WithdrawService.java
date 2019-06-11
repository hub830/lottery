package top.lemna.account.persistence.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baidu.fsg.uid.UidGenerator;
import lombok.extern.slf4j.Slf4j;
import top.lemna.account.exceptions.WithdrawStatusException;
import top.lemna.account.model.constant.BillType;
import top.lemna.account.model.constant.WithdrawChannel;
import top.lemna.account.model.constant.WithdrawStatus;
import top.lemna.account.persistence.domain.Withdraw;
import top.lemna.account.persistence.repository.WithdrawRepository;
import top.lemna.data.jpa.exception.RecordNotExistException;
import top.lemna.data.jpa.service.BaseService;

@Slf4j
@Service
@Transactional
public class WithdrawService extends BaseService<Withdraw> {

  @Autowired
  private WithdrawRepository repository;

  @Autowired
  private UidGenerator uidGenerator;

  @Autowired
  private AccountService accountService;


  private Withdraw findByWithdrawNo(Long withdrawNo) {
    Optional<Withdraw> optional = repository.findByWithdrawNo(withdrawNo);
    return optional.orElseThrow(() -> new RecordNotExistException(String.valueOf(withdrawNo)));
  }


  private Withdraw create(Long accountNo, Long amount, WithdrawChannel channel, String remark) {
    long uid = uidGenerator.getUID();
    String accountInfo = "";
    Withdraw withDraw = new Withdraw(uid, accountNo, accountInfo, amount, channel, remark);
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
  public Withdraw place(Long accountNo, Long amount, WithdrawChannel channel, String remark) {
    Withdraw withdraw = create(accountNo, amount, channel, remark);
    accountService.debit(accountNo, amount, withdraw.getWithdrawNo(), BillType.WITHDRAW_CASH,
        remark);
    return withdraw;
  }

  /**
   * 提现成功</br>
   * 判断提现状态是否为处理中，如果为处理中，则修改状态为成功。
   *
   * @param withdrawNo 提现定单号
   */
  @Transactional
  public void success(Long withdrawNo) {
    Withdraw withdraw = findByWithdrawNo(withdrawNo);
    if (withdraw.getStatus() != WithdrawStatus.PROCESSING) {
      log.error("提现记录状态不正确，orderNo:{}", withdrawNo);
      throw new WithdrawStatusException(withdrawNo, withdraw.getStatus(),
          WithdrawStatus.PROCESSING);
    }
    withdraw.setStatus(WithdrawStatus.SUCCESS);
    save(withdraw);
  }

  /**
   * 取消提现</br>
   * 判断提现状态是否为处理中，如果为处理中，则修改状态为退款，并将提现款项返回至原账户
   * 
   * @param withdrawNo 提现定单号
   * @param remark 取消原因
   */
  @Transactional
  public void cancel(Long withdrawNo, String remark) {
    Withdraw withdraw = findByWithdrawNo(withdrawNo);

    if (withdraw.getStatus() != WithdrawStatus.PROCESSING) {
      log.error("提现记录状态不正确，orderNo:{}", withdrawNo);
      throw new WithdrawStatusException(withdrawNo, withdraw.getStatus(),
          WithdrawStatus.PROCESSING);
    }
    withdraw.setStatus(WithdrawStatus.REFUND);
    withdraw.setRemark(withdraw.getRemark() + "|" + remark);
    save(withdraw);
    // 将用户账户可用余额增加，并扣减冻结余额
    accountService.credit(withdraw.getAccountNo(), withdraw.getAmount(), withdraw.getWithdrawNo(),
        BillType.WITHDRAW_CASH, remark);

  }

}
