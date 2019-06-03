package top.lemna.data.jpa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = false)
public abstract class AutoIdEntity extends TimeEntity {

  private static final long serialVersionUID = 1L;

  /**
   * 流水号
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(length = 32)
  private Long id;

}