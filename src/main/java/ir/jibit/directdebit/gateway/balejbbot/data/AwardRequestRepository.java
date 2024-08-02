package ir.jibit.directdebit.gateway.balejbbot.data;

import ir.jibit.directdebit.gateway.balejbbot.data.entities.AwardRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AwardRequestRepository extends JpaRepository<AwardRequest, Long> {
}