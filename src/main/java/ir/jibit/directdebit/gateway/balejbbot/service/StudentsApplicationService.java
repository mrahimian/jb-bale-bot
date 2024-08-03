package ir.jibit.directdebit.gateway.balejbbot.service;

import ir.jibit.directdebit.gateway.balejbbot.data.AwardRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.AwardRequestRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.StudentRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.entities.AwardRequest;
import ir.jibit.directdebit.gateway.balejbbot.exceptions.BotException;
import ir.jibit.directdebit.gateway.balejbbot.service.models.students.Student;
import org.springframework.transaction.annotation.Transactional;

import static ir.jibit.directdebit.gateway.balejbbot.exceptions.Error.INSUFFICIENT_SCORE;

public class StudentsApplicationService {
    private final StudentRepository studentRepository;
    private final AwardRepository awardRepository;
    private final AwardRequestRepository awardRequestRepositoryٕ;


    public StudentsApplicationService(StudentRepository studentRepository, AwardRepository awardRepository,
                                      AwardRequestRepository awardRequestRepositoryٕ) {

        this.studentRepository = studentRepository;
        this.awardRepository = awardRepository;
        this.awardRequestRepositoryٕ = awardRequestRepositoryٕ;
    }

    public Student getInfo(String chatId) {
        var student = studentRepository.findStudentByChatId(chatId);
        return new Student(student.getId(),
                student.getUsername(), null, student.getFirstName(), student.getLastName(), student.getNationalCode(),
                student.getBirthDate(), student.getPhoneNumber(), student.getFathersPhoneNumber(), student.getMothersPhoneNumber(),
                student.getTeacher().getLastName(), student.getScore());
    }

    public int getScore(String chatId) {
        return studentRepository.findStudentByChatId(chatId).getScore();
    }

    @Transactional
    public void requestForAward(String chatId, int awardCode) {
        var student = studentRepository.findStudentByChatId(chatId);
        var award = awardRepository.findAwardByCode(awardCode);
        var requiredScore = award.getRequiredScore();
        var studentScore = student.getScore();
        if (requiredScore > studentScore) {
            throw new BotException(INSUFFICIENT_SCORE);
        }

        student.setScore(studentScore - requiredScore);
        awardRequestRepositoryٕ.save(new AwardRequest(student, award));
    }
}
