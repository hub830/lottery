package top.lemna.core.constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.common.collect.ImmutableMap;

public enum Status {
  ENABLE("允许"), //
  DISABLE("禁止") //
  ;//

  private String name;

  private Status(String name) {
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
    Status[] values = values();
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    for (Status item : values) {
      list.add(item.toMap());
    }
    return list;
  }
}
