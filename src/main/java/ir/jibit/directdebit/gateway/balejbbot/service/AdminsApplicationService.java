package ir.jibit.directdebit.gateway.balejbbot.service;

import ir.jibit.directdebit.gateway.balejbbot.data.AdminRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.GiftTimeRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.StudentRepository;
import ir.jibit.directdebit.gateway.balejbbot.exceptions.BotException;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.AdminConsumerHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.AdminFunctionHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.AdminSupplierHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.admin.GetMyStudentsHandler;
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
    private final AdminFunctionHandler<String, List<Student>> getMyStudentsHandler;
    private final AdminConsumerHandler<UpdateScoreModel> updateScoreHandler;
    private final AdminConsumerHandler<Boolean> giftsTimeHandler;

    public AdminsApplicationService(StudentRepository studentRepository, AdminRepository adminRepository,
                                    GiftTimeRepository giftTimeRepository) {

        getStudentsHandler = new GetStudentsHandler(studentRepository, adminRepository);
        getMyStudentsHandler = new GetMyStudentsHandler(studentRepository);
        updateScoreHandler = new UpdateStudentsScoreHandler(studentRepository);
        giftsTimeHandler = new GiftsTimeHandler(giftTimeRepository);
    }

    public List<Student> getStudents(Role role) {
        if (getStudentsHandler.isAllowed(role)) {
            return getStudentsHandler.get();
        }

        throw new BotException(PERMISSION_DENIED);
    }

    public List<Student> getMyStudents(Role role, String id) {
        if (getMyStudentsHandler.isAllowed(role)) {
            return getMyStudentsHandler.apply(id);
        }

        throw new BotException(PERMISSION_DENIED);
    }

    public void updateStudentScore(Role role, String[] studentChatId, int score, boolean increase) {
        if (updateScoreHandler.isAllowed(role)) {
            updateScoreHandler.accept(new UpdateScoreModel(studentChatId, score, increase));
            return;
        }

        throw new BotException(PERMISSION_DENIED);
    }


    public void updateGiftsTime(Role role, boolean enable) {
        if (giftsTimeHandler.isAllowed(role)) {
            giftsTimeHandler.accept(enable);
            return;
        }

        throw new BotException(PERMISSION_DENIED);
    }

}
