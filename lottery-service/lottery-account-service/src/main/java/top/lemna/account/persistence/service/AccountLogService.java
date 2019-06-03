package top.lemna.account.persistence.service;

import com.baidu.fsg.uid.UidGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import net.zhongss.kaolabao.core.base.service.BaseService;
import net.zhongss.kaolabao.core.enums.BillType;
import net.zhongss.kaolabao.core.enums.RecordType;
import net.zhongss.kaolabao.core.persistence.account.domain.AvailableBalanceLog;
import net.zhongss.kaolabao.core.persistence.account.repository.AvailableBalanceLogRepository;
import net.zhongss.kaolabao.core.persistence.account.service.dto.AvailableDto;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountLogService extends BaseService<AvailableBalanceLog> {

  @SuppressWarnings("unused")
  @Autowired
  private AvailableBalanceLogRepository repository;

  @Autowired
  private AccountService accountService;

  @Autowired
  private UidGenerator uidGenerator;

  public AvailableBalanceLog create(Long amount,Long accountNo, Long orderNo, Long externalNo,
      Long beforeBalance, Long afterBalance, BillType type, String remark, RecordType recordType) {
    AvailableBalanceLog log = new AvailableBalanceLog(accountNo, orderNo, externalNo, beforeBalance,
        afterBalance, type, remark,amount,recordType);
    return save(log);
  }

  public void saveAll(Set<AvailableBalanceLog> availableBalanceLogSet) {
    repository.saveAll(availableBalanceLogSet);
  }

  public Page<AvailableBalanceLog> findAll(AvailableDto dto, Pageable pageable) {
    Specification<AvailableBalanceLog> specification = getWhereClause(dto);
    return repository.findAll(specification, pageable);
  }

  private Specification<AvailableBalanceLog> getWhereClause(AvailableDto dto) {
    return new Specification<AvailableBalanceLog>() {
      private static final long serialVersionUID = 1L;

      @Override
      public Predicate toPredicate(Root<AvailableBalanceLog> root, CriteriaQuery<?> query,
          CriteriaBuilder cb) {
        List<Predicate> predicate = new ArrayList<>();
        predicate.add(cb.equal(root.get("accountId"), dto.getAccountId()));
        if(dto.getStatus() != null){
          predicate.add(cb.equal(root.get("type"), dto.getStatus()));
        }
        if(StringUtils.isNotBlank(dto.getStartTime())){
          DateTime dt = new DateTime(dto.getStartTime());
          predicate.add(cb.greaterThanOrEqualTo(root.get("createTime"), dt.millisOfDay().withMinimumValue().toDate()));
        }
        if(StringUtils.isNotBlank(dto.getEndTime())){
          DateTime dt = new DateTime(dto.getEndTime());
          predicate.add(cb.lessThanOrEqualTo(root.get("createTime"), dt.millisOfDay().withMaximumValue().toDate()));
        }
        Predicate[] pre = new Predicate[predicate.size()];
        return query.where(predicate.toArray(pre)).getRestriction();
      }
    };
  }
}
