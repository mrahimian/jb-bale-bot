package ir.jibit.directdebit.gateway.balejbbot.service.handlers.admin;

import ir.jibit.directdebit.gateway.balejbbot.data.StudentRepository;
import ir.jibit.directdebit.gateway.balejbbot.service.models.students.Student;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
public class GetStudentsHandler implements Supplier<List<Student>> {
    private final StudentRepository studentRepository;

    public GetStudentsHandler(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> get() {
        return studentRepository.findAll().stream().map(student -> new Student(student.getId(),
                student.getUsername(), null, student.getFirstName(), student.getLastName(), student.getNationalCode(),
                student.getBirthDate(), student.getPhoneNumber(), student.getFathersPhoneNumber(), student.getMothersPhoneNumber(),
                student.getTeacher().getLastName(), student.getScore())).toList();
    }


}
