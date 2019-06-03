package top.lemna.account.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.lemna.account.model.constant.AccountDirection;
import top.lemna.account.model.constant.BillType;
import top.lemna.data.jpa.entity.AutoIdEntity;

/**
 * 可用余额流水表
 * 
 * @author toyota
 *
 */
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(//
    indexes = {//
        @Index(name = "idx_an_en_d", columnList = "accountNo,externalNo,direction")//
    }//
)
public class AccountLog extends AutoIdEntity {

  private static final long serialVersionUID = 929088731917456529L;

  /** 账户编号 */
  @Column(length = 32, nullable = false, unique = true)
  private Long logNo;

  /** 账户编号 */
  @Column(length = 32, nullable = false)
  private Long accountNo;

  /** 外部定单号，如提现订单号或分润明细订单号 */
  @Column(length = 32, nullable = false)
  private Long externalNo;

  /** 账户变动前总金额 单位为分 */
  @Column(length = 16, nullable = false)
  private Long beforeBalance;

  /** 账户变动后总金额 单位为分 */
  @Column(length = 16, nullable = false)
  private Long afterBalance;

  /** 账户变动金额 单位为分 */
  @Column(length = 16, nullable = false)
  private Long amount;

  /** 交易类型 */
  @Column(length = 32, nullable = false)
  @Enumerated(EnumType.STRING)
  private BillType type;

  /**
   * 备注 用于记录资金去向或来源</br>
   * 提现时记录提现账号</br>
   * 消费时记录消费内容</br>
   */
  @Column(length = 512)
  private String remark;

  /** 账户方向 用于区分是增加余额还是减去余额 */
  @Column(length = 32, nullable = false)
  @Enumerated(EnumType.STRING)
  private AccountDirection direction;
}
