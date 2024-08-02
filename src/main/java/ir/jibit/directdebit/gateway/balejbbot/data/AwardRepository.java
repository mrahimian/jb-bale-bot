package ir.jibit.directdebit.gateway.balejbbot.data;

import ir.jibit.directdebit.gateway.balejbbot.data.entities.Award;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AwardRepository extends JpaRepository<Award, Long> {
}