package top.lemna.account.persistence.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baidu.fsg.uid.UidGenerator;
import lombok.extern.slf4j.Slf4j;
import top.lemna.account.exceptions.RechargeStatusException;
import top.lemna.account.model.constant.BillType;
import top.lemna.account.model.constant.RechargeChannel;
import top.lemna.account.model.constant.RechargeStatus;
import top.lemna.account.persistence.domain.Recharge;
import top.lemna.account.persistence.repository.RechargeRepository;
import top.lemna.data.jpa.exception.RecordNotExistException;
import top.lemna.data.jpa.service.BaseService;

@Slf4j
@Service
@Transactional
public class RechargeService extends BaseService<Recharge> {

  @Autowired
  private RechargeRepository repository;

  @Autowired
  private UidGenerator uidGenerator;

  @Autowired
  private AccountService accountService;


  private Recharge findByRechargeNo(Long recharge) {
    Optional<Recharge> optional = repository.findByRechargeNo(recharge);
    return optional.orElseThrow(() -> new RecordNotExistException(String.valueOf(recharge)));
  }

  /**
   * 充值
   * 
   * @param rechargeNo
   * @param accountNo
   * @param amount
   * @param channel
   * @param remark
   * @return
   */
  public Recharge create(Long accountNo, Long amount, RechargeChannel channel, String remark) {
    long uid = uidGenerator.getUID();
    Recharge recharge = new Recharge(uid, accountNo, amount, channel);
    recharge.setRemark(remark);
    return save(recharge);
  }

  /**
   * 充值成功
   * 
   * @param rechargeNo
   * @param remark
   */
  @Transactional
  public void success(Long rechargeNo, String remark) {
    Recharge recharge = findByRechargeNo(rechargeNo);
    if (recharge.getStatus() != RechargeStatus.PROCESSING) {
      log.error("充值记录状态不正确，orderNo:{}", rechargeNo);
      throw new RechargeStatusException(rechargeNo, recharge.getStatus(),
          RechargeStatus.PROCESSING);
    }
    recharge.setStatus(RechargeStatus.SUCCESS);
    recharge.setRemark(recharge.getRemark() + "|" + remark);
    // 将用户账户可用余额增加
    accountService.debit(recharge.getAccountNo(), recharge.getAmount(), recharge.getRechargeNo(),
        BillType.WITHDRAW_CASH, remark);
    save(recharge);
  }

  /**
   * 充值失败
   * 
   * @param rechargeNo
   * @param remark
   */
  @Transactional
  public void cancel(Long rechargeNo, String remark) {
    Recharge recharge = findByRechargeNo(rechargeNo);

    if (recharge.getStatus() != RechargeStatus.PROCESSING) {
      log.error("充值记录状态不正确，orderNo:{}", rechargeNo);
      throw new RechargeStatusException(rechargeNo, recharge.getStatus(),
          RechargeStatus.PROCESSING);
    }
    recharge.setStatus(RechargeStatus.CANCEL);
    recharge.setRemark(recharge.getRemark() + "|" + remark);
    save(recharge);

  }
}
