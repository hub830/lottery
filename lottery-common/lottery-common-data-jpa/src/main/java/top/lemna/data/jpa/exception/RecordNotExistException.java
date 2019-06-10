package top.lemna.data.jpa.exception;

import top.lemna.core.exceptions.LotteryRuntimeException;

public class RecordNotExistException extends LotteryRuntimeException {
  private static final long serialVersionUID = 1L;

  private String recordId;

  private String recordNo;

  public RecordNotExistException(Long recordId) {
    super(format("数据库中不存在对应的记录, recordId:{}", recordId));
    this.recordId = String.valueOf(recordId);
  }

  public RecordNotExistException(String recordId) {
    super(format("数据库中不存在对应的记录, recordId:{}", recordId));
    this.recordId = recordId;
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
