package top.lemna.account.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.lemna.account.persistence.domain.Account;
import top.lemna.account.persistence.repository.AccountRepository;
import top.lemna.data.jpa.service.BaseService;

@Service
@Transactional
public class AccountService extends BaseService<Account> {

  @Autowired
  private AccountRepository repository;


}
