package ir.jibit.directdebit.gateway.balejbbot.data;

import ir.jibit.directdebit.gateway.balejbbot.data.entities.GiftTime;
import ir.jibit.directdebit.gateway.balejbbot.data.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GiftTimeRepository extends JpaRepository<GiftTime, Long> {

}