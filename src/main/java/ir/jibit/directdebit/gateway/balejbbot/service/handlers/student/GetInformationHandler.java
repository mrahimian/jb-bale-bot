package ir.jibit.directdebit.gateway.balejbbot.service.handlers.student;

import ir.jibit.directdebit.gateway.balejbbot.data.StudentRepository;
import ir.jibit.directdebit.gateway.balejbbot.service.models.students.Student;

import java.util.function.Function;

public class GetInformationHandler implements Function<String, Student> {
    private final StudentRepository studentRepository;

    public GetInformationHandler(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student apply(String chatId) {
        var student = studentRepository.findStudentByChatId(chatId);
        return new Student(student.getId(),
                student.getUsername(), null, student.getFirstName(), student.getLastName(), student.getNationalCode(),
                student.getBirthDate(), student.getPhoneNumber(), student.getFathersPhoneNumber(), student.getMothersPhoneNumber(),
                student.getTeacher().getLastName(), student.getScore());
    }
}
