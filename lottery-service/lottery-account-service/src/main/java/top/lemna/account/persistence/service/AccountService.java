package net.zhongss.kaolabao.core.persistence.account.service;

import com.baidu.fsg.uid.UidGenerator;
import java.util.Optional;
import net.zhongss.kaolabao.core.base.service.BaseService;
import net.zhongss.kaolabao.core.enums.BillType;
import net.zhongss.kaolabao.core.enums.RecordType;
import net.zhongss.kaolabao.core.enums.YesNo;
import net.zhongss.kaolabao.core.exception.AccountAlreadyExistException;
import net.zhongss.kaolabao.core.exception.AccountBalanceNotEnoughException;
import net.zhongss.kaolabao.core.exception.AccountBlockException;
import net.zhongss.kaolabao.core.exception.RecordNotExistException;
import net.zhongss.kaolabao.core.persistence.account.domain.Account;
import net.zhongss.kaolabao.core.persistence.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService extends BaseService<Account> {

  @Autowired
  private UidGenerator uidGenerator;

  @Autowired
  private AccountLogService balanceService;

  @Autowired
  private AccountRepository repository;

  @Autowired
  private AccountLogService availableBalanceLogService;


  public Account findByIdForUpdate(Long accountId) {
    Optional<Account> t = repository.findByIdForUpdate(accountId);
    return t.orElseThrow(() -> new RecordNotExistException(accountId));
  }

  /**
   * 创建新账户
   * 
   * @param accountName
   * @return
   */
  @Transactional
  public Account create(String accountName) {
    Optional<Account> optional = repository.findByAccountName(accountName);
    if (optional.isPresent()) {
      throw new AccountAlreadyExistException(accountName);
    }
    long uid = uidGenerator.getUID();
    Account account = new Account(uid, accountName);
    return save(account);
  }

  /**
   * 贷记操作 收款 账户余额增加
   * 
   * @param accountNo
   * @param amount
   */
  @Transactional
  public void credit(Long accountNo, Long amount, Long externalNo, BillType type, String remark) {
    Account account = findByIdForUpdate(accountNo);
    Long beforeBalance = account.getAvailableBalance();
    account.setAvailableBalance(beforeBalance + amount);
    Long afterBalance = account.getAvailableBalance();

    long orderNo = uidGenerator.getUID();
    // 创建交易 流水
    balanceService.create(amount,accountNo, orderNo, externalNo, beforeBalance, afterBalance, type,
        remark, RecordType.CREDIT);

    save(account);
  }



  /**
   * 
   * 借记操作 付款 账户余额减少
   * 
   * @param accountNo
   * @param externalNo
   * @param amount
   * @param type
   * @param remark
   */
  @Transactional
  void debit(Long accountNo, Long amount, Long externalNo, BillType type, String remark) {
    Account account = findByIdForUpdate(accountNo);
    // 判断账户是否被锁定，被锁定的账号不允许进行借记操作
    if (account.getBlock() == YesNo.YES) {
      throw new AccountBlockException(account);
    }
    // 判断账户是否余额充足
    if (account.getAvailableBalance() < amount) {
      throw new AccountBalanceNotEnoughException(account, amount);
    }

    Long beforeBalance = account.getAvailableBalance();
    account.setAvailableBalance(beforeBalance - amount);
    Long afterBalance = account.getAvailableBalance();

    long orderNo = uidGenerator.getUID();
    // 创建交易 流水
    balanceService.create(amount,accountNo, orderNo, externalNo, beforeBalance, afterBalance, type,
        remark,RecordType.DEBIT);
    save(account);
  }

  public Account findByAccountNo(Long accountNo) {
    Optional<Account> t = repository.findById(accountNo);
    return t.orElseThrow(() -> new RecordNotExistException(accountNo));
  }
/*
  @Transactional
  public void saveCommission(AgentSettleCommission as) {
    Optional<Account> optional = repository.findByIdForUpdate(as.getAgentNo());
    Account account = optional.get();

    String remark = as.getAgentNo() + ":" + as.getOrderNo() + ":" + BillType.COMMISSION;
    long commission = 0;
    if(as.getCommissionType() == CommissionType.SELF){
      commission = as.getSelfCommission();
    }else if(as.getCommissionType() == CommissionType.DIFF){
      commission = as.getDiffCommission();
    }
    AvailableBalanceLog ab = new AvailableBalanceLog(as.getAgentNo(),
        uidGenerator.getUID(), as.getOrderNo(),
        account.getAvailableBalance(), account.getAvailableBalance() + commission,
        BillType.COMMISSION,
        remark);
    account.setAvailableBalance(account.getAvailableBalance() + commission);
    repository.save(account);
    availableBalanceLogService.save(ab);
  }*/
}
