package ir.jibit.directdebit.gateway.balejbbot.service.handlers.student;

import ir.jibit.directdebit.gateway.balejbbot.data.AwardRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.AwardRequestRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.StudentRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.entities.AwardRequest;
import ir.jibit.directdebit.gateway.balejbbot.exceptions.BotException;
import ir.jibit.directdebit.gateway.balejbbot.service.models.students.AwardRequestModel;
import ir.jibit.directdebit.gateway.balejbbot.service.models.students.Student;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;
import java.util.function.Function;

import static ir.jibit.directdebit.gateway.balejbbot.exceptions.Error.INSUFFICIENT_SCORE;

public class AwardRequestHandler implements Consumer<AwardRequestModel> {
    private final StudentRepository studentRepository;
    private final AwardRepository awardRepository;
    private final AwardRequestRepository awardRequestRepository;

    public AwardRequestHandler(StudentRepository studentRepository, AwardRepository awardRepository,
                                      AwardRequestRepository awardRequestRepository){

        this.studentRepository = studentRepository;
        this.awardRepository = awardRepository;
        this.awardRequestRepository = awardRequestRepository;
    }

    @Transactional
    @Override
    public void accept(AwardRequestModel awardRequestModel) {
        var student = studentRepository.findStudentByChatId(awardRequestModel.chatId());
        var award = awardRepository.findAwardByCode(awardRequestModel.awardCode());
        var requiredScore = award.getRequiredScore();
        var studentScore = student.getScore();
        if (requiredScore > studentScore) {
            throw new BotException(INSUFFICIENT_SCORE);
        }

        student.setScore(studentScore - requiredScore);
        awardRequestRepository.save(new AwardRequest(student, award));
    }
}
