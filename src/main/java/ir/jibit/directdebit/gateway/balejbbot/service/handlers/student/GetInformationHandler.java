package ir.jibit.directdebit.gateway.balejbbot.service.handlers.student;

import ir.jibit.directdebit.gateway.balejbbot.data.AdminRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.StudentRepository;
import ir.jibit.directdebit.gateway.balejbbot.service.models.students.Student;

import java.util.function.Function;

public class GetInformationHandler implements Function<String, Student> {
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;

    public GetInformationHandler(StudentRepository studentRepository, AdminRepository adminRepository) {
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public Student apply(String chatId) {
        var student = studentRepository.findStudentByChatId(chatId);
        var teacher = adminRepository.findById(student.getTeacherId());
        return new Student(student.getId(),
                student.getUsername(), null, student.getFirstName(), student.getLastName(), student.getNationalCode(),
                student.getBirthDate(), student.getPhoneNumber(), student.getFathersPhoneNumber(), student.getMothersPhoneNumber(),
                teacher.isPresent() ? "آقای " + teacher.get().getLastName() : "", student.getScore());
    }
}
