package top.lemna.account.exceptions;

import top.lemna.core.exceptions.LotteryRuntimeException;

public class AccountBlockException extends LotteryRuntimeException {
	private static final long serialVersionUID = 5770390658812498742L;

	public AccountBlockException(Long accountNo, String accountName) {
		super(format("账户被锁定,accountNo:{} ,accountName:{}", accountNo, accountName));
	}
}
