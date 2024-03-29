package top.lemna.account.persistence.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import top.lemna.account.persistence.domain.Recharge;
import top.lemna.data.jpa.repository.BaseRepository;

@Repository
public interface RechargeRepository extends BaseRepository<Recharge,Long> {

  Optional<Recharge> findByRechargeNo(Long rechargeNo);
}
