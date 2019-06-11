package top.lemna.account.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.lemna.account.model.constant.SettleAccountType;
import top.lemna.account.model.constant.SettleType;
import top.lemna.account.model.dto.AlipaySettleAccountInfoDto;
import top.lemna.account.model.dto.BankSettleAccountInfoDto;
import top.lemna.account.model.dto.SettleAccountInfoDto;
import top.lemna.account.model.dto.WeixinSettleAccountInfoDto;
import top.lemna.data.jpa.entity.AutoIdEntity;

/**
 * @Auth: mqy
 * @Date: 2019/3/30
 * @Time: 16:16 结算账号信息
 */
@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(//
    name = "account_settle_account", //
    indexes = {@Index(name = "idx_an_at", columnList = "accountNo,accountType")} //
)
public class SettleAccount extends AutoIdEntity {

  private static final long serialVersionUID = 3147732997051690019L;

  /** 账户编号 */
  @Column(length = 32, nullable = false, unique = true)
  private Long accountNo;


  /** 结算类型 */
  @NotNull
  @Column(length = 32)
  @Enumerated(EnumType.STRING)
  private SettleType settleType;


  /** 账户类型 */
  @NotNull
  @Column(length = 32)
  @Enumerated(EnumType.STRING)
  private SettleAccountType accountType;

  /** 结算账号 JSON数据，依据账号类型为微信或银行卡而保存不同的数据类型 */
  @NotNull
  @Column(length = 1024)
  private String settleAccountInfo;

  @SuppressWarnings({"unchecked", "rawtypes"})
  public SettleAccountInfoDto getSettleAccountInfo() {
    Class tClass = null;
    switch (accountType) {
      case ALIPAY:
        tClass = AlipaySettleAccountInfoDto.class;
        break;
      case BANK:
        tClass = BankSettleAccountInfoDto.class;
        break;
      case WXPAY:
        tClass = WeixinSettleAccountInfoDto.class;
        break;
    }
    return getSettleAccountInfo(tClass);
  }


  private <T extends SettleAccountInfoDto> T getSettleAccountInfo(Class<T> tClass) {
    return JSON.parseObject(settleAccountInfo, tClass);
  }

  public void setSettleAccountInfo(SettleAccountInfoDto accountInfo) {
    this.settleAccountInfo = JSON.toJSONString(accountInfo);
  }


  public SettleAccount(Long accountNo, SettleType settleType, SettleAccountType accountType) {
    super();
    this.accountNo = accountNo;
    this.settleType = settleType;
    this.accountType = accountType;
  }


}
