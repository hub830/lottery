package top.lemna.account.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.lemna.core.exceptions.LotteryRuntimeException;

@Data
@EqualsAndHashCode(callSuper=false)
public class AccountBalanceNotEnoughException extends LotteryRuntimeException {
	private static final long serialVersionUID = 5770390658812498742L;

	/** 账户编号 */
	private Long accountNo;

	/** 账户名称 */
	private String accountName;

	/** 账户余额 */
	Long balance;

	/** 实际消费金额 */
	private Long amount;

	public AccountBalanceNotEnoughException(Long accountNo, String accountName, Long balance, Long amount) {
		super(format("账户余额不足 accountNo:{} ,accountName:{}, balance:{}, amount:{}", accountNo, accountName, balance,
				amount));
		this.accountNo = accountNo;
		this.accountName = accountName;
		this.balance = balance;
		this.amount = amount;
	}

}
