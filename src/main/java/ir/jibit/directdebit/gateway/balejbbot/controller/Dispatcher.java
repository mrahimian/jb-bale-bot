package ir.jibit.directdebit.gateway.balejbbot.controller;

import ir.jibit.directdebit.gateway.balejbbot.data.AdminRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.StudentRepository;
import ir.jibit.directdebit.gateway.balejbbot.exceptions.BotException;
import ir.jibit.directdebit.gateway.balejbbot.service.AdminsApplicationService;
import ir.jibit.directdebit.gateway.balejbbot.service.CommonApplicationService;
import ir.jibit.directdebit.gateway.balejbbot.service.StudentsApplicationService;

import static ir.jibit.directdebit.gateway.balejbbot.exceptions.Error.INVALID_CREDENTIALS;

public class Dispatcher {
    private final CommonApplicationService commonApplicationService;
    private final AdminsApplicationService adminsApplicationService;
    private final StudentsApplicationService studentsApplicationService;
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;

    public Dispatcher(CommonApplicationService commonApplicationService, AdminsApplicationService adminsApplicationService,
                      StudentsApplicationService studentsApplicationService, StudentRepository studentRepository, AdminRepository adminRepository) {

        this.commonApplicationService = commonApplicationService;
        this.adminsApplicationService = adminsApplicationService;
        this.studentsApplicationService = studentsApplicationService;
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
    }

    private void dispatch(String message) {

    }

    private void isAllowed(String chatId, boolean isStudent) {
        if (isStudent) {
            var student = studentRepository.findStudentByChatId(chatId);
            if (student == null) {
                throw new BotException(INVALID_CREDENTIALS);
            }

        } else {
            var admin = adminRepository.findAdminByChatId(chatId);
            if (admin == null) {
                throw new BotException(INVALID_CREDENTIALS);
            }
        }
    }
}
