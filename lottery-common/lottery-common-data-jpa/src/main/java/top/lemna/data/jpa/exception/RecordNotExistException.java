package top.lemna.data.jpa.exception;

import java.text.MessageFormat;
import top.lemna.core.exceptions.LotteryRuntimeException;

public class RecordNotExistException extends LotteryRuntimeException {
  private static final long serialVersionUID = 1L;

  private String recordId;

  private String recordNo;

  public RecordNotExistException(String recordId) {
    super(format("数据库中不存在对应的记录, recordId:{}", recordId));
    recordId = recordId.toString();
  }

  public RecordNotExistException(String recordId, String recordNo) {
    super(MessageFormat.format("数据库中不存在对应的记录, recordId:{}, recordNo:{}", recordId, recordNo));
    recordId = recordNo.toString();
  }

  public String getRecordId() {
    return recordId;
  }

  public void setRecordId(String recordId) {
    this.recordId = recordId;
  }

  public String getRecordNo() {
    return recordNo;
  }

  public void setRecordNo(String recordNo) {
    this.recordNo = recordNo;
  }

}
