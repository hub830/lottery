package top.lemna.account.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.lemna.account.model.constant.RechargeStatus;
import top.lemna.core.exceptions.LotteryRuntimeException;

@Data
@EqualsAndHashCode(callSuper = false)
public class RechargeStatusException extends LotteryRuntimeException {

  private static final long serialVersionUID = 1L;

  /** 提升订单号 */
  private Long rechargeNo;

  /** 源状态 */
  private RechargeStatus sourceStatus;


  /** 期待状态 */
  private RechargeStatus expectStatus;


  public RechargeStatusException(Long rechargeNo, RechargeStatus sourceStatus,
      RechargeStatus expectStatus) {
    super(format("充值状态不正确 withdrawNo:{} ,sourceStatus:{}, expectStatus:{}", rechargeNo,
        sourceStatus, expectStatus));
    this.rechargeNo = rechargeNo;
    this.sourceStatus = sourceStatus;
    this.expectStatus = expectStatus;
  }

}
