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
public class BankSettleAccountInfoDto extends SettleAccountInfoDto {

  /** 身份证号 */
  private String identityNo;

  private DepositBankDto depositBank;

  public BankSettleAccountInfoDto(String accountName, String accountNo, String identityNo,
      DepositBankDto depositBank) {
    super(accountName, accountNo);
    this.identityNo = identityNo;
    this.depositBank = depositBank;
  }
}
