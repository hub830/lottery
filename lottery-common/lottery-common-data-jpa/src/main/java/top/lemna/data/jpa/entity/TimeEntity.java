package top.lemna.data.jpa.entity;

import java.util.Date;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.apache.commons.lang3.builder.ToStringBuilder;
import lombok.Data;
import top.lemna.data.jpa.listener.TimeEntityListener;


@Data
@MappedSuperclass
@EntityListeners(TimeEntityListener.class)
public abstract class TimeEntity implements java.io.Serializable {

  private static final long serialVersionUID = 1L;

  @Version
  private Integer optimistic;

  /**
   * 创建时间
   */
  @Temporal(TemporalType.TIMESTAMP)
  private Date createTime = new Date();

  /**
   * 更新时间
   */
  @Temporal(TemporalType.TIMESTAMP)
  private Date updateTime;

  public TimeEntity() {
    super();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}