package ir.jibit.directdebit.gateway.balejbbot.service.handlers.admin;

import ir.jibit.directdebit.gateway.balejbbot.data.StudentRepository;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.AdminFunctionHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Role;
import ir.jibit.directdebit.gateway.balejbbot.service.models.students.Student;
import org.springframework.stereotype.Service;

import java.util.List;

import static ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Permission.FETCH_MY_STUDENTS_LIST;

@Service
public class GetMyStudentsHandler implements AdminFunctionHandler<String, List<Student>> {
    private final StudentRepository studentRepository;

    public GetMyStudentsHandler(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> apply(String id) {
        return studentRepository.findAllByTeacherId(id).stream().map(student -> new Student(student.getId(), student.getFirstName(),
                student.getLastName(), student.getBirthDate(), student.getScore())).toList();
    }

    @Override
    public boolean isAllowed(Role role) {
        return role.getPermissions().stream().anyMatch(FETCH_MY_STUDENTS_LIST::equals);
    }
}
