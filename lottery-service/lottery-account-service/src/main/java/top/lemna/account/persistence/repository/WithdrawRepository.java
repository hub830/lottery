package top.lemna.account.persistence.repository;

import org.springframework.stereotype.Repository;
import top.lemna.account.persistence.domain.Withdraw;
import top.lemna.data.jpa.repository.BaseRepository;

@Repository
public interface WithdrawRepository extends BaseRepository<Withdraw, Long> {

  Withdraw findByWithdrawNo(Long withdrawNo);
}
