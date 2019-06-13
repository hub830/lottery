package top.lemna.account.web.admin;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.lemna.account.persistence.domain.Account;

@RestController
@RequestMapping("/account")
public class AccountController 
{
 

  @GetMapping
  public List<Account> list() {
 
    return null;
  }

}
