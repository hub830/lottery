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
@EqualsAndHashCode(callSuper=false)
public class BankSettleAccountInfoDto extends SettleAccountInfoDto{


  /** 身份证号 */
  private String identityNo;

  /** 开户名 */
  private String accountName;

  /** 开户行名称 */
  private String bankName;

  /** 联行号 */
  private String alliedBankCode;

  /** 开户名银行卡号 */
  private String bankAccountNo;

  /** 开户行总行编号 */
  private String bankCode;
  
  /** 区域 */
  private String area;

}
