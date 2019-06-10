package top.lemna.account.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.lemna.account.model.constant.WithdrawStatus;
import top.lemna.core.exceptions.LotteryRuntimeException;

@Data
@EqualsAndHashCode(callSuper = false)
public class WithdrawStatusException extends LotteryRuntimeException {

  private static final long serialVersionUID = 1L;

  /** 提升订单号 */
  private Long withdrawNo;

  /** 源状态 */
  private WithdrawStatus sourceStatus;


  /** 目标状态 */
  private WithdrawStatus targeetStatus;


  public WithdrawStatusException(Long withdrawNo, WithdrawStatus sourceStatus,
      WithdrawStatus targeetStatus) {
    super(format("提现状态不正确 withdrawNo:{} ,sourceStatus:{}, targeetStatus:{}, amount:{}", withdrawNo,
        sourceStatus, targeetStatus));
    this.withdrawNo = withdrawNo;
    this.sourceStatus = sourceStatus;
    this.targeetStatus = targeetStatus;
  }

}
