package top.lemna.account.model.constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.common.collect.ImmutableMap;

/**
 * 充值状态
 * 
 * @author fox
 *
 */
public enum RechargeStatus {
  PROCESSING("处理中"), //
  SUCCESS("成功"), //
  REFUND("失败")//
  ;//

  private String name;

  private RechargeStatus(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public Map<String, String> toMap() {
    return ImmutableMap.<String, String>builder()//
        .put("name", name)//
        .put("value", toString())//
        .build();
  }

  public static List<Map<String, String>> maps() {
    RechargeStatus[] values = values();
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    for (RechargeStatus item : values) {
      list.add(item.toMap());
    }
    return list;
  }
}
