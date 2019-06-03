package top.lemna.account.persistence.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.lemna.core.constants.YesNo;
import top.lemna.data.jpa.entity.AutoIdEntity; 

/**
 * 产品表
 * 
 * @author toyota
 *
 */
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Account extends AutoIdEntity {

  private static final long serialVersionUID = 299101453982987455L;

  /** 账户编号 */
  @Column(length = 32, nullable = false, unique = true)
  private Long accountNo;

  /** 户名 */
  @Column(length = 64, nullable = false)
  private String accountName;

  /** 可用余额 单位为分 */
  @Column(length = 16, nullable = false)
  private Long balance;

  /** 是否锁定 当状态状态为锁定状态时不允许提现，只允许入账操作 */
  @Column(length = 32, nullable = false)
  @Enumerated(EnumType.STRING)
  private YesNo block;

}
