package top.lemna.account.persistence.repository;

import org.springframework.stereotype.Repository;
import top.lemna.account.persistence.domain.SettleAccount;
import top.lemna.data.jpa.repository.BaseRepository;

@Repository
public interface SettleAccountRepository extends BaseRepository<SettleAccount,Long> {
}
