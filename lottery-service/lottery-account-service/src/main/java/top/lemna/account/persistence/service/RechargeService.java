package top.lemna.account.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.lemna.account.persistence.domain.Recharge;
import top.lemna.account.persistence.repository.RechargeRepository;
import top.lemna.data.jpa.service.BaseService;

@Service
@Transactional
public class RechargeService extends BaseService<Recharge> {

  @Autowired
  private RechargeRepository repository;


}
