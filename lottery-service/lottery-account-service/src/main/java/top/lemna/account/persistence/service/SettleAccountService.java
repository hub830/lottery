package top.lemna.account.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.lemna.account.persistence.domain.SettleAccount;
import top.lemna.account.persistence.repository.SettleAccountRepository;
import top.lemna.data.jpa.service.BaseService;

@Service
@Transactional
public class SettleAccountService extends BaseService<SettleAccount> {

  @Autowired
  private SettleAccountRepository repository;


}
