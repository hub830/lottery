package top.lemna.uid;

import org.apache.commons.lang3.StringUtils;
import com.baidu.fsg.uid.exception.UidGenerateException;


public class ShortUidGenerator {
  
  private String idName;
  
  private int idLength;

  private ShortUidAssigner shortUidAssigner;

  
  /**
   * 
   * @param idName 用于做为关键字持久化id
   * @param idLength 生成的id的长度，不足长度的，除最左边位为1外，其它位补零
   * @param shortUidAssigner
   */
  public ShortUidGenerator(String idName, int idLength, ShortUidAssigner shortUidAssigner) {
    super();
    this.idName = idName;
    this.idLength = idLength;
    this.shortUidAssigner = shortUidAssigner;
  }


  long getUID() throws UidGenerateException {
    return generatorId();
  }


  private Long generatorId() {
    Long increment = shortUidAssigner.assignWorkerId(idName);
    
    // 转为string
    String incStr = increment + "";
    // 不足六位进行补0
    if (incStr.length() < idLength-1) {
      incStr = StringUtils.leftPad(incStr, idLength-1, '0');
    }
    String customerNo = "1" + incStr;
    return Long.parseLong(customerNo);
  }
  
}
