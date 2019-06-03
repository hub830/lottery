package top.lemna.core.exceptions;


public class LotteryRuntimeException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public LotteryRuntimeException(String message) {
    super(message);
  }

  public LotteryRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  public static String format(String str, Object... args) {
    for (int i = 0; i < args.length; i++) {
      str = str.replaceFirst("\\{\\}", String.valueOf(args[i]));
    }
    return str;
  }
}
