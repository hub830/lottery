package top.lemna.account.persistence.repository;

import org.springframework.stereotype.Repository;
import top.lemna.account.persistence.domain.AccountLog;
import top.lemna.data.jpa.repository.BaseRepository;

@Repository
public interface AccountLogRepository extends BaseRepository<AccountLog,Long> {
}
