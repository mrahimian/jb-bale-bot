package ir.jibit.directdebit.gateway.balejbbot.service;

import ir.jibit.directdebit.gateway.balejbbot.data.AdminRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.StudentRepository;
import ir.jibit.directdebit.gateway.balejbbot.exceptions.BotException;
import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Admin;
import ir.jibit.directdebit.gateway.balejbbot.service.models.students.Student;
import org.springframework.stereotype.Service;

import java.util.List;

import static ir.jibit.directdebit.gateway.balejbbot.exceptions.Error.INVALID_CREDENTIALS;

@Service
public class CommonApplicationService {
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;

    public CommonApplicationService(StudentRepository studentRepository, AdminRepository adminRepository) {
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
    }

    public void insertStudents(List<Student> students) {

    }

    public void insertAdmins(List<Admin> admins) {

    }

    public void login(boolean isStudent, String username, String password, String chatId) {
        if (isStudent) {
            var result = studentRepository.findStudentByUsernameAndPassword(username, password);
            if (result.isEmpty()) {
                throw new BotException(INVALID_CREDENTIALS);
            }

            var student = result.get();
            student.setChatId(chatId);
            studentRepository.save(student);
        } else {
            var result = adminRepository.findAdminByUsernameAndPassword(username, password);
            if (result.isEmpty()) {
                throw new BotException(INVALID_CREDENTIALS);
            }

            var admin = result.get();
            admin.setChatId(chatId);
            adminRepository.save(admin);
        }
    }
}
