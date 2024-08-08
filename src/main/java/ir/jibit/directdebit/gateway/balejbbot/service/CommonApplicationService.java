package ir.jibit.directdebit.gateway.balejbbot.service;

import ir.jibit.directdebit.gateway.balejbbot.data.AdminRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.AwardRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.StudentRepository;
import ir.jibit.directdebit.gateway.balejbbot.exceptions.BotException;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.AdminConsumerHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.common.InsertAdminHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.common.InsertAwardsHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.common.InsertStudentsHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.student.GetAwardsHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.models.Award;
import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Admin;
import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Role;
import ir.jibit.directdebit.gateway.balejbbot.service.models.students.Student;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

import static ir.jibit.directdebit.gateway.balejbbot.exceptions.Error.*;

@Service
public class CommonApplicationService {
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;
    private final AdminConsumerHandler<List<Student>> insertStudentsHandler;
    private final AdminConsumerHandler<List<Admin>> insertAdminsHandler;
    private final AdminConsumerHandler<List<Award>> insertAwardsHandler;
    private final Supplier<List<Award>> getAwardsHandler;

    public CommonApplicationService(StudentRepository studentRepository, AdminRepository adminRepository,
                                    AwardRepository awardRepository) {
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
        this.insertStudentsHandler = new InsertStudentsHandler(studentRepository);
        this.insertAdminsHandler = new InsertAdminHandler(adminRepository);
        this.insertAwardsHandler = new InsertAwardsHandler(awardRepository);
        this.getAwardsHandler = new GetAwardsHandler(awardRepository);
    }

    public String insertStudents(List<Student> students, Role role) {
        if (insertAwardsHandler.isAllowed(role)) {
            insertStudentsHandler.accept(students);
            return "با موفقیت ثبت شد.";
        }

        throw new BotException(PERMISSION_DENIED);
    }

    public String insertAdmins(List<Admin> admins, Role role) {
        if (insertAdminsHandler.isAllowed(role)) {
            insertAdminsHandler.accept(admins);
            return "با موفقیت ثبت شد.";
        }

        throw new BotException(PERMISSION_DENIED);
    }

    public String insertAwards(List<Award> awards, Role role) {
        if (insertAwardsHandler.isAllowed(role)) {
            insertAwardsHandler.accept(awards);
            return "با موفقیت ثبت شد.";
        }

        throw new BotException(PERMISSION_DENIED);
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

    public List<Award> getAwards(String chatId, boolean isStudent) {
        if ((isStudent && studentRepository.existsStudentByChatId(chatId)) ||
                (!isStudent && adminRepository.existsAdminByChatId(chatId))) {

            return getAwardsHandler.get();
        }

        throw new BotException(UNRECOGNIZED_USER);
    }
}
