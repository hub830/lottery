package top.lemna.account.web.rpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.lemna.account.model.dto.CreateAccountDto;
import top.lemna.account.model.vo.AccountVo;
import top.lemna.account.persistence.service.AccountService;
import top.lemna.account.service.AccountFeignApi;

 @RefreshScope
 @RestController
public class AccountFeignClient implements AccountFeignApi {

  @Autowired
  private AccountService service;

   @Override
  @PostMapping(value = "/api/Account/reducestock")
  public AccountVo create(@RequestBody CreateAccountDto createAccountDto) {
 
    AccountVo vo = new AccountVo();
    return vo;
  }

}
