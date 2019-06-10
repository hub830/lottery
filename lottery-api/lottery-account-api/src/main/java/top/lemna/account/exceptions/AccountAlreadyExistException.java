package top.lemna.account.exceptions;

import java.text.MessageFormat;
import top.lemna.core.exceptions.LotteryRuntimeException;

public class AccountAlreadyExistException extends LotteryRuntimeException {
  private static final long serialVersionUID = 5770390658812498742L;

  public AccountAlreadyExistException(String accountName) {
    super(format(accountName));
  }

  public AccountAlreadyExistException(Long accountNo) {
    super(format(accountNo));
  }

  static String format(String accountName) {
    return MessageFormat.format("名称为[{0}]的账号已经存在", //
        accountName);
  }

  static String format(Long accountNo) {
    return MessageFormat.format("编号为[{0}]的账号已经存在", //
        accountNo);
  }
}
