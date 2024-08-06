package ir.jibit.directdebit.gateway.balejbbot.service;

import ir.jibit.directdebit.gateway.balejbbot.data.AwardRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.AwardRequestRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.GiftTimeRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.StudentRepository;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.student.AwardRequestHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.student.GetAwardsHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.student.GetInformationHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.models.Award;
import ir.jibit.directdebit.gateway.balejbbot.service.models.students.AwardRequestModel;
import ir.jibit.directdebit.gateway.balejbbot.service.models.students.Student;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class StudentsApplicationService {
    private final StudentRepository studentRepository;
    private final Function<String, Student> getInformationHandler;
    private final Consumer<AwardRequestModel> awardRequestHandler;
    private final Supplier<List<Award>> getAwardsHandler;


    public StudentsApplicationService(StudentRepository studentRepository, AwardRepository awardRepository,
                                      AwardRequestRepository awardRequestRepository, GiftTimeRepository giftTimeRepository) {

        this.studentRepository = studentRepository;
        getInformationHandler = new GetInformationHandler(studentRepository);
        awardRequestHandler = new AwardRequestHandler(studentRepository, awardRepository, awardRequestRepository, giftTimeRepository);
        getAwardsHandler = new GetAwardsHandler(awardRepository);

    }

    public Student getInfo(String chatId) {
        return getInformationHandler.apply(chatId);
    }

    public int getScore(String chatId) {
        return studentRepository.findStudentByChatId(chatId).getScore();
    }

    public List<Award> getAwards() {
        return getAwardsHandler.get();
    }

    public void requestForAward(String chatId, int awardCode) {
        awardRequestHandler.accept(new AwardRequestModel(chatId, awardCode));
    }
}
