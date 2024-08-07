package ir.jibit.directdebit.gateway.balejbbot.service;

import ir.jibit.directdebit.gateway.balejbbot.data.AdminRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.StudentRepository;
import ir.jibit.directdebit.gateway.balejbbot.exceptions.BotException;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.AdminConsumerHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.common.InsertStudentsHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Admin;
import ir.jibit.directdebit.gateway.balejbbot.service.models.students.Student;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static ir.jibit.directdebit.gateway.balejbbot.exceptions.Error.INVALID_CREDENTIALS;

@Service
public class CommonApplicationService {
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;
    private final AdminConsumerHandler<List<Student>> insertStudentsHandler;

    public CommonApplicationService(StudentRepository studentRepository, AdminRepository adminRepository) {
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
        this.insertStudentsHandler = new InsertStudentsHandler(studentRepository);
    }

    public String insertStudents(List<Student> students) {
        insertStudentsHandler.accept(students);
        return "با موفقیت ثبت شد";
    }

    public void insertAdmins(List<Admin> admins) {

    }

    public String login(String chatId, String username, String password, boolean isStudent) {
        password = DigestUtils.md5Hex(password);
        if (isStudent) {
            var result = studentRepository.findStudentByUsernameAndPassword(username, password);
            if (result.isEmpty()) {
                throw new BotException(INVALID_CREDENTIALS);
            }

            var student = result.get();
            student.setChatId(chatId);
            studentRepository.save(student);
            return student.getFirstName() + " " + student.getLastName();
        } else {
            var result = adminRepository.findAdminByUsernameAndPassword(username, password);
            if (result.isEmpty()) {
                throw new BotException(INVALID_CREDENTIALS);
            }

            var admin = result.get();
            admin.setChatId(chatId);
            adminRepository.save(admin);
            return admin.getFirstName() + " " + admin.getLastName();
        }
    }
}
