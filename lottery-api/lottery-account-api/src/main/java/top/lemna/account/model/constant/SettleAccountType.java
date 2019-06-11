package top.lemna.account.model.constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.common.collect.ImmutableMap;

/**
 * @Auth: mqy
 * @Date: 2019/3/30
 * @Time: 16:28 结算卡类型
 */
public enum SettleAccountType {
  BANK("银行卡"), //
  WXPAY("微信"), //
  ALIPAY("支付宝")//
  ;//

  private String name;

  private SettleAccountType(String name) {
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
    SettleAccountType[] values = values();
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    for (SettleAccountType item : values) {
      list.add(item.toMap());
    }
    return list;
  }
}
