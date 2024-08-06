package ir.jibit.directdebit.gateway.balejbbot.service.handlers.admin;

import ir.jibit.directdebit.gateway.balejbbot.data.StudentRepository;
import ir.jibit.directdebit.gateway.balejbbot.exceptions.BotException;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.AdminConsumerHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.AdminSupplierHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Role;
import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.UpdateScoreModel;
import ir.jibit.directdebit.gateway.balejbbot.service.models.students.Student;
import org.springframework.stereotype.Service;

import java.util.List;

import static ir.jibit.directdebit.gateway.balejbbot.exceptions.Error.PERMISSION_DENIED;
import static ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Permission.FETCH_STUDENTS_LIST;
import static ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Permission.UPDATE_STUDENTS_SCORE;

@Service
public class UpdateStudentsScoreHandler implements AdminConsumerHandler<UpdateScoreModel> {
    private final StudentRepository studentRepository;

    public UpdateStudentsScoreHandler(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void accept(UpdateScoreModel model) {
        var student = studentRepository.findStudentByChatId(model.studentChatId());
        var teacherId = model.teacherId();
        if (teacherId == null || teacherId.isEmpty() || !teacherId.equals(student.getTeacher().getId())) {
            throw new BotException(PERMISSION_DENIED);
        }

        if (model.increase()) {
            student.setScore(student.getScore() + model.score());
        } else {
            student.setScore(student.getScore() - model.score());
        }
        studentRepository.save(student);
    }

    @Override
    public boolean isAllowed(Role role) {
        return role.getPermissions().stream().anyMatch(UPDATE_STUDENTS_SCORE::equals);
    }
}
