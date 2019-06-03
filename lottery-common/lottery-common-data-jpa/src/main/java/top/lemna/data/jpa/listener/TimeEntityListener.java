package top.lemna.data.jpa.listener;


import java.util.Date;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import top.lemna.data.jpa.entity.TimeEntity;

public class TimeEntityListener {
  @PrePersist
  public void prePersist(TimeEntity entity) {
    entity.setCreateTime(new Date());
//    entity.setUpdateTime(new Date());
  }

  @PreUpdate
  public void preUpdate(TimeEntity entity) {
    entity.setUpdateTime(new Date());
  }
}
