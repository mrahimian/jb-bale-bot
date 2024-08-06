package ir.jibit.directdebit.gateway.balejbbot.service;

import ir.jibit.directdebit.gateway.balejbbot.data.AdminRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.GiftTimeRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.StudentRepository;
import ir.jibit.directdebit.gateway.balejbbot.exceptions.BotException;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.AdminConsumerHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.AdminSupplierHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.admin.GetStudentsHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.admin.GiftsTimeHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.admin.UpdateStudentsScoreHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Role;
import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.UpdateScoreModel;
import ir.jibit.directdebit.gateway.balejbbot.service.models.students.Student;
import org.springframework.stereotype.Service;

import java.util.List;

import static ir.jibit.directdebit.gateway.balejbbot.exceptions.Error.PERMISSION_DENIED;

@Service
public class AdminsApplicationService {
    private final AdminSupplierHandler<List<Student>> getStudentsHandler;
    private final AdminConsumerHandler<UpdateScoreModel> updateScoreHandler;
    private final AdminConsumerHandler<Boolean> giftsTimeHandler;

    public AdminsApplicationService(StudentRepository studentRepository, AdminRepository adminRepository, GiftTimeRepository giftTimeRepository) {

        getStudentsHandler = new GetStudentsHandler(studentRepository);
        updateScoreHandler = new UpdateStudentsScoreHandler(studentRepository);
        giftsTimeHandler = new GiftsTimeHandler(giftTimeRepository);
    }

    public List<Student> getStudents(Role role) {
        if (getStudentsHandler.isAllowed(role)) {
            return getStudentsHandler.get();
        }

        throw new BotException(PERMISSION_DENIED);
    }

    public void updateStudentScore(Role role, String teacherId, String studentChatId, int score, boolean increase) {
        if (updateScoreHandler.isAllowed(role)) {
            updateScoreHandler.accept(new UpdateScoreModel(teacherId, studentChatId, score, increase));
        }

        throw new BotException(PERMISSION_DENIED);
    }


    public void updateGiftsTime(Role role, boolean enable) {
        if (giftsTimeHandler.isAllowed(role)) {
            giftsTimeHandler.accept(enable);
        }

        throw new BotException(PERMISSION_DENIED);
    }

}
