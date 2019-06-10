package top.lemna.account.persistence.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import top.lemna.account.persistence.domain.Withdraw;
import top.lemna.data.jpa.repository.BaseRepository;

@Repository
public interface WithdrawRepository extends BaseRepository<Withdraw, Long> {

  Optional<Withdraw> findByWithdrawNo(Long withdrawNo);
}
