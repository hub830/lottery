package top.lemna.account.persistence.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import top.lemna.account.persistence.domain.Account;
import top.lemna.data.jpa.repository.BaseRepository;

@Repository
public interface AccountRepository extends BaseRepository<Account,Long> {
  Optional<Account> findByAccountNo(String AccountNo);
}
