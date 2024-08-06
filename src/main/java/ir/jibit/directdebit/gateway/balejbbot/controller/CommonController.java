package ir.jibit.directdebit.gateway.balejbbot.controller;

import ir.jibit.directdebit.gateway.balejbbot.service.CommonApplicationService;
import org.springframework.stereotype.Controller;

@Controller
public class CommonController {
    private final CommonApplicationService commonApplicationService;

    public CommonController(CommonApplicationService commonApplicationService) {
        this.commonApplicationService = commonApplicationService;
    }

    public String login(String chatId, String username, String password, boolean isStudent) {
        var name = commonApplicationService.login(chatId, username, password, isStudent);
        return name + " " + "اطلاعات شما با موفقیت ثبت شد ✅";
    }
}
