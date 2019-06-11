package top.lemna.account.model.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * 结算账号类型信息
 * 
 * @author fox
 *
 */
@Data
@NoArgsConstructor
public class SettleAccountInfoDto {

  /** 结算账号名称 */
  @NotNull
  private String accountName;

  /** 结算账号 */
  @NotNull
  private String accountNo;

  public SettleAccountInfoDto(@NotNull String accountName, @NotNull String accountNo) {
    super();
    this.accountName = accountName;
    this.accountNo = accountNo;
  }
}
