package top.lemna.account.persistence.repository;

import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import top.lemna.account.persistence.domain.Account;
import top.lemna.data.jpa.repository.BaseRepository;

@Repository
public interface AccountRepository extends BaseRepository<Account,Long> {
  
  Optional<Account> findByAccountNo(Long accountNo);
  
  Optional<Account> findByAccountName(String accountName);

  @Lock(value = LockModeType.PESSIMISTIC_WRITE)
  @Query(value = "select t from Account t where t.accountNo =?1 ")
  Optional<Account> findByAccountNoForUpdate(Long accountNo);
}
