package ir.jibit.directdebit.gateway.balejbbot.data;

import ir.jibit.directdebit.gateway.balejbbot.data.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findStudentByChatId(String chatId);
}