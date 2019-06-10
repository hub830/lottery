package top.lemna.account.persistence.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.lemna.account.model.constant.WithdrawChannel;
import top.lemna.account.model.constant.WithdrawStatus;
import top.lemna.data.jpa.entity.AutoIdEntity;

/**
 * 提现记录表
 *
 * @author toyota
 */
@Data
@Entity
@Table(name = "account_withdraw")
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Withdraw extends AutoIdEntity {

  private static final long serialVersionUID = 299101453982987455L;

  /**
   * 提现订单编号
   */
  @Column(length = 32, nullable = false)
  private Long withdrawNo;

  /**
   * 账号
   */
  @Column(length = 32, nullable = false)
  private Long accountNo;

  /**
   * 账号信息，根据提现类型的不同，放入不同的JSON对象
   */
  @Column(length = 1024, nullable = false)
  private String accountInfo;

  /**
   * 提现金额 单位为分
   */
  @Column(precision = 12, scale = 2, nullable = false)
  private Long amount;

  /**
   * 提现渠道
   */
  @Column(length = 32, nullable = false)
  @Enumerated(EnumType.STRING)
  private WithdrawChannel channel;

  /**
   * 提现状态
   */
  @Column(length = 32, nullable = false)
  @Enumerated(EnumType.STRING)
  private WithdrawStatus status;

  /**
   * 备注</br>
   * 如果是提现到银行卡 请按 开户行|户名|卡号|证件号 拼写数据 如 建设银行|张三|43921234567820|10010120011010001</br>
   * 如果是提现到支付宝或微信，则在此处填写支付宝或微信账号</br>
   * 另提现失败后将失败的原因拼接在原有备注后
   */
  @Column(length = 256)
  private String remark;

  public Withdraw(Long withdrawNo, Long accountNo, String accountInfo, Long amount,
      WithdrawChannel channel, String remark) {
    super();
    this.withdrawNo = withdrawNo;
    this.accountNo = accountNo;
    this.accountInfo = accountInfo;
    this.amount = amount;
    this.channel = channel;
    this.status = WithdrawStatus.PENDING;
    this.remark = remark;
  }


}
