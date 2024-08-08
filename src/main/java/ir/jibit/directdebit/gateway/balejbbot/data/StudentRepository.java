package ir.jibit.directdebit.gateway.balejbbot.data;

import ir.jibit.directdebit.gateway.balejbbot.data.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    Student findStudentByChatId(String chatId);

    Optional<Student> findStudentByUsernameAndPassword(String username, String password);

    boolean existsStudentByChatId(String chatId);

    List<Student> findAllByOrderByTeacherId();

    List<Student> findAllByTeacherId(String teacherId);
}