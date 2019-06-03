package top.lemna.account.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateAccountDto {

  /**
   * 账号名称
   */
  private String name;

}
