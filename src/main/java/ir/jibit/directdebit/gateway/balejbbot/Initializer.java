package ir.jibit.directdebit.gateway.balejbbot;

import ir.jibit.directdebit.gateway.balejbbot.data.AdminRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.GiftTimeRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.entities.Admin;
import ir.jibit.directdebit.gateway.balejbbot.data.entities.GiftTime;
import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Role;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class Initializer implements CommandLineRunner {

    private final GiftTimeRepository giftTimeRepository;
    private final AdminRepository adminRepository;

    @Override
    public void run(String... args) {
        if (!giftTimeRepository.existsById("0")) {
            giftTimeRepository.save(new GiftTime("0", false));
        }

        if (!adminRepository.existsAdminByRole(Role.MANAGER)) {
            adminRepository.save(Admin.builder()
                    .id("1000")
                    .username("rahim")
                    .password(DigestUtils.md5Hex("rahim"))
                    .firstName("محمدرضا")
                    .lastName("رحیمیان")
                    .role(Role.MANAGER)
                    .build());
        }
    }
}
