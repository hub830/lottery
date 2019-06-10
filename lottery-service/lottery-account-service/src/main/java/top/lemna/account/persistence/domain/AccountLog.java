package top.lemna.account.persistence.domain;

import javax.annotation.Generated;
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

  /** 流水编号 */
  @Column(length = 32, nullable = false, unique = true)
  private Long logNo;

  /** 账户编号 */
  @Column(length = 32, nullable = false)
  private Long accountNo;

  /** 外部定单号，如提现订单号或分润明细订单号 */
  @Column(length = 32, nullable = false)
  private String externalNo;

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

  @Generated("SparkTools")
  private AccountLog(Builder builder) {
    this.logNo = builder.logNo;
    this.accountNo = builder.accountNo;
    this.externalNo = builder.externalNo;
    this.beforeBalance = builder.beforeBalance;
    this.afterBalance = builder.afterBalance;
    this.amount = builder.amount;
    this.type = builder.type;
    this.remark = builder.remark;
    this.direction = builder.direction;
  }

  /**
   * Creates builder to build {@link AccountLog}.
   * @return created builder
   */
  @Generated("SparkTools")
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link AccountLog}.
   */
  @Generated("SparkTools")
  public static final class Builder {
    private Long logNo;
    private Long accountNo;
    private String externalNo;
    private Long beforeBalance;
    private Long afterBalance;
    private Long amount;
    private BillType type;
    private String remark;
    private AccountDirection direction;

    private Builder() {}

    public Builder withLogNo(Long logNo) {
      this.logNo = logNo;
      return this;
    }

    public Builder withAccountNo(Long accountNo) {
      this.accountNo = accountNo;
      return this;
    }

    public Builder withExternalNo(String externalNo) {
      this.externalNo = externalNo;
      return this;
    }

    public Builder withBeforeBalance(Long beforeBalance) {
      this.beforeBalance = beforeBalance;
      return this;
    }

    public Builder withAfterBalance(Long afterBalance) {
      this.afterBalance = afterBalance;
      return this;
    }

    public Builder withAmount(Long amount) {
      this.amount = amount;
      return this;
    }

    public Builder withType(BillType type) {
      this.type = type;
      return this;
    }

    public Builder withRemark(String remark) {
      this.remark = remark;
      return this;
    }

    public Builder withDirection(AccountDirection direction) {
      this.direction = direction;
      return this;
    }

    public AccountLog build() {
      return new AccountLog(this);
    }
  }
}
