package ir.jibit.directdebit.gateway.balejbbot.service.handlers.student;

import ir.jibit.directdebit.gateway.balejbbot.data.AwardRepository;
import ir.jibit.directdebit.gateway.balejbbot.data.StudentRepository;
import ir.jibit.directdebit.gateway.balejbbot.service.models.Award;
import ir.jibit.directdebit.gateway.balejbbot.service.models.students.Student;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class GetAwardsHandler implements Supplier<List<Award>> {
    private final AwardRepository awardRepository;

    public GetAwardsHandler(AwardRepository awardRepository) {
        this.awardRepository = awardRepository;
    }

    @Override
    public List<Award> get() {
        return awardRepository.findAll().stream()
                .map(award -> new Award(award.getId(), award.getName(), award.getCode(), award.getDescription(),
                        award.getRequiredScore()))
                .toList();
    }
}
