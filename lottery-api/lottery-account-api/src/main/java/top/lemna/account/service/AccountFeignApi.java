
package top.lemna.account.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.lemna.account.model.dto.CreateAccountDto;
import top.lemna.account.model.vo.AccountVo;



@FeignClient(value = "lottery-account-service")
public interface AccountFeignApi {

  @PostMapping(value = "/api/account")
  AccountVo create(@RequestBody CreateAccountDto createAccountDto);
}

