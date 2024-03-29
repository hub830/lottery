package top.lemna.account.exceptions;

import top.lemna.core.exceptions.LotteryRuntimeException;

public class AccountBizException extends LotteryRuntimeException {
  private static final long serialVersionUID = 1L;

  public AccountBizException(String message) {
    super(message);
  }

  public AccountBizException(String message, Throwable cause) {
    super(message, cause);
  }

  public static String format(String str, Object... args) {
    for (int i = 0; i < args.length; i++) {
      str = str.replaceFirst("\\{\\}", String.valueOf(args[i]));
    }
    return str;
  }
}
