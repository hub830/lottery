package top.lemna.account.model.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 *   结算账号类型信息
 * 
 * @author fox
 *
 */
@Data
@NoArgsConstructor
public class SettleAccountInfoDto {

  /** 结算账号名称 */
  @NotNull
  private String settleAccountName;

  /** 结算账号 */
  @NotNull
  private String settleAccountNo;
}
