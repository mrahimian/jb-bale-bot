package ir.jibit.directdebit.gateway.balejbbot.controller;

import ir.jibit.directdebit.gateway.balejbbot.data.AdminRepository;
import ir.jibit.directdebit.gateway.balejbbot.exceptions.BotException;
import ir.jibit.directdebit.gateway.balejbbot.service.AdminsApplicationService;
import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Role;
import ir.jibit.directdebit.gateway.balejbbot.service.models.students.Student;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static ir.jibit.directdebit.gateway.balejbbot.exceptions.Error.UNRECOGNIZED_USER;

@Controller
public class AdminController {
    private final AdminsApplicationService adminsApplicationService;
    private final AdminRepository adminRepository;

    public AdminController(AdminsApplicationService adminsApplicationService, AdminRepository adminRepository) {
        this.adminsApplicationService = adminsApplicationService;
        this.adminRepository = adminRepository;
    }

    public String getStudents(String chatId) {
        var admin = adminRepository.findAdminByChatId(chatId);
        if (admin != null) {
            var stringBuilder = new StringBuilder();
            var teacher = new AtomicReference<>("");
            adminsApplicationService.getStudents(admin.getRole()).forEach(student -> {
                if (!teacher.get().equals(student.getTeacher())) {
                    stringBuilder.append("*").append("گروه آقای ").append(student.getTeacher()).append("* : \n");
                    teacher.set(student.getTeacher());
                }
                stringBuilder.append(student.toString()).append("\n\n");
            });

            return stringBuilder.toString();
        } else {
            throw new BotException(UNRECOGNIZED_USER);
        }
    }

    public String getMyStudents(String chatId) {
        var admin = adminRepository.findAdminByChatId(chatId);
        if (admin != null) {
            var stringBuilder = new StringBuilder();
            var myStudents = adminsApplicationService.getMyStudents(admin.getRole(), admin.getId());
            if (myStudents.isEmpty()){
                return "هیچ متربی‌ای ندارید !";
            }
            myStudents.forEach(student -> {
                stringBuilder.append(student.summary()).append("\n");
            });

            return stringBuilder.toString();
        } else {
            throw new BotException(UNRECOGNIZED_USER);
        }
    }

    public String increaseStudentScore(String chatId, String[] studentChatId, int scoreToIncrease) {
        var admin = adminRepository.findAdminByChatId(chatId);
        if (admin != null) {
            adminsApplicationService.updateStudentScore(admin.getRole(), studentChatId, scoreToIncrease, true);
            return "درخواست شما با موفقیت انجام شد✅";
        } else {
            throw new BotException(UNRECOGNIZED_USER);
        }
    }

    public String decreaseStudentScore(String chatId, String[] studentChatId, int scoreToIncrease) {
        var admin = adminRepository.findAdminByChatId(chatId);
        if (admin != null) {
            adminsApplicationService.updateStudentScore(admin.getRole(), studentChatId, scoreToIncrease, false);
            return "درخواست شما با موفقیت انجام شد✅";
        } else {
            throw new BotException(UNRECOGNIZED_USER);
        }
    }

    public String enableGiftsTime(String chatId) {
        var admin = adminRepository.findAdminByChatId(chatId);
        if (admin != null) {
            adminsApplicationService.updateGiftsTime(admin.getRole(), true);
            return "کمد جوایز فعال شد ✅";
        } else {
            throw new BotException(UNRECOGNIZED_USER);
        }
    }

    public String disableGiftsTime(String chatId) {
        var admin = adminRepository.findAdminByChatId(chatId);
        if (admin != null) {
            adminsApplicationService.updateGiftsTime(admin.getRole(), false);
            return "کمد جوایز غیر فعال شد ❌";
        } else {
            throw new BotException(UNRECOGNIZED_USER);
        }
    }
}
