package top.lemna.account.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.lemna.account.model.constant.SettleAccountType;
import top.lemna.account.model.constant.SettleType;
import top.lemna.account.model.dto.SettleAccountInfoDto;
import top.lemna.account.persistence.domain.SettleAccount;
import top.lemna.account.persistence.repository.SettleAccountRepository;
import top.lemna.data.jpa.service.BaseService;

@Service
@Transactional
public class SettleAccountService extends BaseService<SettleAccount> {

  @Autowired
  private SettleAccountRepository repository;

  public SettleAccount create(Long accountNo, SettleType settleType, SettleAccountType accountType,
      SettleAccountInfoDto accountInfo) {

    SettleAccount settleAccount = new SettleAccount(accountNo, settleType, accountType);
    settleAccount.setSettleAccountInfo(accountInfo);
    
    return save(settleAccount);
  }
}
