package ir.jibit.directdebit.gateway.balejbbot.service.handlers.student;

import ir.jibit.directdebit.gateway.balejbbot.data.AwardRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.AwardRequestRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.GiftTimeRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.StudentRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.entities.AwardRequest;
import ir.jibit.directdebit.gateway.balejbbot.exceptions.BotException;
import ir.jibit.directdebit.gateway.balejbbot.service.models.students.AwardRequestModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

import static ir.jibit.directdebit.gateway.balejbbot.exceptions.Error.*;

public class AwardRequestHandler implements Consumer<AwardRequestModel> {
    private final StudentRepository studentRepository;
    private final AwardRepository awardRepository;
    private final AwardRequestRepository awardRequestRepository;
    private final GiftTimeRepository giftTimeRepository;

    public AwardRequestHandler(StudentRepository studentRepository, AwardRepository awardRepository,
                               AwardRequestRepository awardRequestRepository, GiftTimeRepository giftTimeRepository) {

        this.studentRepository = studentRepository;
        this.awardRepository = awardRepository;
        this.awardRequestRepository = awardRequestRepository;
        this.giftTimeRepository = giftTimeRepository;
    }

    @Transactional
    @Override
    public void accept(AwardRequestModel awardRequestModel) {
        if (giftTimeRepository.findById(0L).get().isActive()) {
            var student = studentRepository.findStudentByChatId(awardRequestModel.chatId());
            var award = awardRepository.findAwardByCode(awardRequestModel.awardCode());
            if (award == null) {
                throw new BotException(AWARD_NOT_EXISTS);
            }

            var requiredScore = award.getRequiredScore();
            var studentScore = student.getScore();
            if (requiredScore > studentScore) {
                throw new BotException(INSUFFICIENT_SCORE);
            }

            student.setScore(studentScore - requiredScore);
            awardRequestRepository.save(new AwardRequest(student, award));
        } else {
            throw new BotException(GIFTS_TIME_NOT_ACTIVE);
        }
    }
}
