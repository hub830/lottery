package top.lemna.account.persistence.domain;


import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.lemna.data.jpa.entity.AutoIdEntity; 

/**
 * 产品表
 * 
 * @author toyota
 *
 */
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Account extends AutoIdEntity {

  private static final long serialVersionUID = 299101453982987455L;


  
}
