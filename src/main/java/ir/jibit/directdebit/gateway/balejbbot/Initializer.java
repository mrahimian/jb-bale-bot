package ir.jibit.directdebit.gateway.balejbbot;

import ir.jibit.directdebit.gateway.balejbbot.data.GiftTimeRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.entities.GiftTime;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class Initializer implements CommandLineRunner {

    private final GiftTimeRepository giftTimeRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!giftTimeRepository.existsById(0L)) {
            giftTimeRepository.save(new GiftTime(0, false));
        }
    }
}
