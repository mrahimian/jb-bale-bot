package ir.jibit.directdebit.gateway.balejbbot.service.handlers.admin;

import ir.jibit.directdebit.gateway.balejbbot.data.StudentRepository;
import ir.jibit.directdebit.gateway.balejbbot.exceptions.BotException;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.AdminConsumerHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Role;
import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.UpdateScoreModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Permission.UPDATE_STUDENTS_SCORE;

@Service
public class UpdateStudentsScoreHandler implements AdminConsumerHandler<UpdateScoreModel> {
    private final StudentRepository studentRepository;

    public UpdateStudentsScoreHandler(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    @Override
    public void accept(UpdateScoreModel model) {
        Arrays.stream(model.studentIds()).parallel().forEach(studentId -> {

            var student = studentRepository.findById(Long.valueOf(studentId));
//        var teacherId = model.teacherId();
//        if (teacherId == null || teacherId.isEmpty() || !teacherId.equals(student.getTeacher().getId())) {
//            throw new BotException(PERMISSION_DENIED);
//        }
            if (student.isEmpty()) {
                throw new BotException(String.format("متربی با شناسه %s یافت نشد", studentId));
            }

            var st = student.get();
            if (model.increase()) {
                st.setScore(st.getScore() + model.score());
            } else {
                st.setScore(st.getScore() - model.score());
            }
            studentRepository.save(st);
        });
    }

    @Override
    public boolean isAllowed(Role role) {
        return role.getPermissions().stream().anyMatch(UPDATE_STUDENTS_SCORE::equals);
    }
}
