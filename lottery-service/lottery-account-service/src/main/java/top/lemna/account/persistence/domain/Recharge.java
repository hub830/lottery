package top.lemna.account.persistence.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.lemna.account.model.constant.RechargeChannel;
import top.lemna.account.model.constant.RechargeStatus;
import top.lemna.data.jpa.entity.AutoIdEntity;

/**
 * 充值记录表
 *
 * @author toyota
 */
@Data
@Entity
@Table(name = "account_recharge")
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Recharge extends AutoIdEntity {

  private static final long serialVersionUID = 299101453982987455L;

  /**
   * 充值订单编号
   */
  @Column(length = 32, nullable = false)
  private Long rechargeNo;

  /**
   * 账号
   */
  @Column(length = 32, nullable = false)
  private Long accountNo;

  /**
   * 充值金额 单位为分
   */
  @Column(precision = 12, scale = 2, nullable = false)
  private Long amount;

  /**
   * 充值渠道
   */
  @Column(length = 32, nullable = false)
  @Enumerated(EnumType.STRING)
  private RechargeChannel channel;

  /**
   * 充值状态
   */
  @Column(length = 32, nullable = false)
  @Enumerated(EnumType.STRING)
  private RechargeStatus status;

  /**
   * 备注</br>
   */
  @Column(length = 256)
  private String remark;

  public Recharge(Long rechargeNo, Long accountNo, Long amount, RechargeChannel channel) {
    super();
    this.rechargeNo = rechargeNo;
    this.accountNo = accountNo;
    this.amount = amount;
    this.channel = channel;
    this.status = RechargeStatus.PROCESSING;
  }
}
