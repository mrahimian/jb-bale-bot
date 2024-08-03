package ir.jibit.directdebit.gateway.balejbbot.service;

import ir.jibit.directdebit.gateway.balejbbot.data.AdminRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.GiftTimeRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.StudentRepository;
import ir.jibit.directdebit.gateway.balejbbot.exceptions.BotException;
import ir.jibit.directdebit.gateway.balejbbot.service.models.students.Student;
import org.springframework.stereotype.Service;

import java.util.List;

import static ir.jibit.directdebit.gateway.balejbbot.exceptions.Error.PERMISSION_DENIED;

@Service
public class AdminsApplicationService {
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;
    private final GiftTimeRepository giftTimeRepository;

    public AdminsApplicationService(StudentRepository studentRepository, AdminRepository adminRepository, GiftTimeRepository giftTimeRepository) {
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
        this.giftTimeRepository = giftTimeRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll().stream().map(student -> new Student(student.getId(),
                student.getUsername(), null, student.getFirstName(), student.getLastName(), student.getNationalCode(),
                student.getBirthDate(), student.getPhoneNumber(), student.getFathersPhoneNumber(), student.getMothersPhoneNumber(),
                student.getTeacher().getLastName(), student.getScore())).toList();
    }

    public void increaseStudentScore(String teacherId, String studentChatId, int scoreToIncrease) {
        var student = studentRepository.findStudentByChatId(studentChatId);
        if (teacherId == null || teacherId.isEmpty() || !teacherId.equals(student.getTeacher().getId())) {
            throw new BotException(PERMISSION_DENIED);
        }

        student.setScore(student.getScore() + scoreToIncrease);
        studentRepository.save(student);
    }

    public void decreaseStudentScore(String teacherId, String studentChatId, int scoreToDecrease) {
        var student = studentRepository.findStudentByChatId(studentChatId);
        if (teacherId == null || teacherId.isEmpty() || !teacherId.equals(student.getTeacher().getId())) {
            throw new BotException(PERMISSION_DENIED);
        }

        student.setScore(student.getScore() - scoreToDecrease);
        studentRepository.save(student);
    }

    public void enableGiftsTime() {
        giftTimeRepository.getReferenceById(0L).setActive(true);
    }

    public void disableGiftsTime() {
        giftTimeRepository.getReferenceById(0L).setActive(false);
    }
}
