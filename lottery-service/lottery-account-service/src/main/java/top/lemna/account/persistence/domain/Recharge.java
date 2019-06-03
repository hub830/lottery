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
   * 提现订单编号
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
   * 提现状态
   */
  @Column(length = 32, nullable = false)
  @Enumerated(EnumType.STRING)
  private RechargeStatus status;

  /**
   * 备注</br>
   * 如果是提现到银行卡 请按 开户行|户名|卡号|证件号 拼写数据 如 建设银行|张三|43921234567820|10010120011010001</br>
   * 如果是提现到支付宝或微信，则在此处填写支付宝或微信账号</br>
   * 另提现失败后将失败的原因拼接在原有备注后
   */
  @Column(length = 256)
  private String remark;


}
