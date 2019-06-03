package top.lemna.account.model.constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.common.collect.ImmutableMap;

public enum WithdrawStatus {
  PENDING("待处理"), //
  PROCESSING("处理中"),
  SUCCESS("成功"), //
  REFUND("失败"),//
  WAIT_AUDIT("待审核")//
  ;//

  private String name;

  private WithdrawStatus(String name) {
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
    WithdrawStatus[] values = values();
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    for (WithdrawStatus item : values) {
      list.add(item.toMap());
    }
    return list;
  }
}
