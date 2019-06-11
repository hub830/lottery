package top.lemna.account.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * 银行 结算账号类型信息
 * 
 * @author fox
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DepositBankDto {

  /** 开户行名称 */
  private String name;

  /** 联行号 */
  private String alliedCode;

  /** 开户行总行编号 */
  private String bankCode;

  /** 区域 */
  private String area;

  public DepositBankDto(String name, String alliedCode, String bankCode, String area) {
    super();
    this.name = name;
    this.alliedCode = alliedCode;
    this.bankCode = bankCode;
    this.area = area;
  }
}
