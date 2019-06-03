package top.lemna.account.model.constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.common.collect.ImmutableMap;

public enum WithdrawChannel {
  BANK("银行卡"), //
  WXPAY("微信"), //
  ALIPAY("支付宝")//
  ;//

  private String name;

  private WithdrawChannel(String name) {
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
    WithdrawChannel[] values = values();
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    for (WithdrawChannel item : values) {
      list.add(item.toMap());
    }
    return list;
  }
}
