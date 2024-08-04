package ir.jibit.directdebit.gateway.balejbbot.controller;

import ir.jibit.directdebit.gateway.balejbbot.data.AdminRepository;
import ir.jibit.directdebit.gateway.balejbbot.exceptions.BotException;
import ir.jibit.directdebit.gateway.balejbbot.service.AdminsApplicationService;
import ir.jibit.directdebit.gateway.balejbbot.service.models.students.Student;
import org.springframework.stereotype.Controller;

import java.util.List;

import static ir.jibit.directdebit.gateway.balejbbot.exceptions.Error.UNRECOGNIZED_USER;

@Controller
public class AdminController {
    private final AdminsApplicationService adminsApplicationService;
    private final AdminRepository adminRepository;
    private final static String SPECIAL_CHARS = "✅⚡ ✨❌⛔⭕";

    public AdminController(AdminsApplicationService adminsApplicationService, AdminRepository adminRepository) {
        this.adminsApplicationService = adminsApplicationService;
        this.adminRepository = adminRepository;
    }

    public List<Student> getStudents(String chatId) {
        if (adminRepository.existsAdminByChatId(chatId)) {
            return adminsApplicationService.getStudents();
        } else {
            throw new BotException(UNRECOGNIZED_USER);
        }
    }

    public String increaseStudentScore(String chatId, String teacherId, String studentChatId, int scoreToIncrease) {
        if (adminRepository.existsAdminByChatId(chatId)) {
            adminsApplicationService.increaseStudentScore(teacherId, studentChatId, scoreToIncrease);
            return "درخواست شما با موفقیت انجام شد✅";
        } else {
            throw new BotException(UNRECOGNIZED_USER);
        }
    }

    public String decreaseStudentScore(String chatId, String teacherId, String studentChatId, int scoreToIncrease) {
        if (adminRepository.existsAdminByChatId(chatId)) {
            adminsApplicationService.decreaseStudentScore(teacherId, studentChatId, scoreToIncrease);
            return "درخواست شما با موفقیت انجام شد✅";
        } else {
            throw new BotException(UNRECOGNIZED_USER);
        }
    }

    public String enableGiftsTime(String chatId) {
        if (adminRepository.existsAdminByChatId(chatId)) {
            adminsApplicationService.enableGiftsTime();
            return "کمد جوایز فعال شد ✅";
        } else {
            throw new BotException(UNRECOGNIZED_USER);
        }
    }

    public String disableGiftsTime(String chatId) {
        if (adminRepository.existsAdminByChatId(chatId)) {
            adminsApplicationService.disableGiftsTime();
            return "کمد جوایز غیر فعال شد ❌";
        } else {
            throw new BotException(UNRECOGNIZED_USER);
        }
    }
}
