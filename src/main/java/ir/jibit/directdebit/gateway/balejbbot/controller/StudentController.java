package ir.jibit.directdebit.gateway.balejbbot.controller;

import ir.jibit.directdebit.gateway.balejbbot.data.GiftTimeRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.StudentRepository;
import ir.jibit.directdebit.gateway.balejbbot.exceptions.BotException;
import ir.jibit.directdebit.gateway.balejbbot.service.StudentsApplicationService;
import org.springframework.stereotype.Controller;

import static ir.jibit.directdebit.gateway.balejbbot.exceptions.Error.UNRECOGNIZED_USER;

@Controller
public class StudentController {
    private final StudentsApplicationService studentsApplicationService;
    private final StudentRepository studentRepository;
    private final GiftTimeRepository giftTimeRepository;

    public StudentController(StudentsApplicationService studentsApplicationService, StudentRepository studentRepository,
                             GiftTimeRepository giftTimeRepository) {

        this.studentsApplicationService = studentsApplicationService;
        this.studentRepository = studentRepository;
        this.giftTimeRepository = giftTimeRepository;
    }

    public String getInfo(String chatId) {
        if (studentRepository.existsStudentByChatId(chatId)) {
            return studentsApplicationService.getInfo(chatId).toString();
        } else {
            throw new BotException(UNRECOGNIZED_USER);
        }
    }

    public String getScore(String chatId) {
        if (studentRepository.existsStudentByChatId(chatId)) {
            var score = studentsApplicationService.getScore(chatId);
            return String.format("امتیاز شما : %d", score);
        } else {
            throw new BotException(UNRECOGNIZED_USER);
        }
    }

    public String getAwards(String chatId) {
        if (studentRepository.existsStudentByChatId(chatId)) {
            return studentsApplicationService.getAwards().toString();
            //todo set return pattern
        } else {
            throw new BotException(UNRECOGNIZED_USER);
        }
    }

    public boolean isGiftTimeEnable(String chatId) {
        if (studentRepository.existsStudentByChatId(chatId)) {
            if (giftTimeRepository.findById(0L).get().isActive()) {
                return true;
            }
        } else {
            throw new BotException(UNRECOGNIZED_USER);
        }

        return false;
    }

    public String requestForAward(String chatId, int awardCode) {
        if (studentRepository.existsStudentByChatId(chatId)) {
            studentsApplicationService.requestForAward(chatId, awardCode);
            return "درخواست جایزه شما با موفقیت ثبت شد \uD83C\uDFC6 ✅ \uD83E\uDD29";
        } else {
            throw new BotException(UNRECOGNIZED_USER);
        }
    }
}
