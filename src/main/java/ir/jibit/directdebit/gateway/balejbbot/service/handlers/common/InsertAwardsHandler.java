package ir.jibit.directdebit.gateway.balejbbot.service.handlers.common;

import ir.jibit.directdebit.gateway.balejbbot.data.AwardRepository;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.AdminConsumerHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.models.Award;
import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Role;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;
import java.util.UUID;

import static ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Permission.INSERT_ADMINS;
import static ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Permission.INSERT_AWARDS;

public class InsertAwardsHandler implements AdminConsumerHandler<List<Award>> {
    private final AwardRepository awardRepository;

    public InsertAwardsHandler(AwardRepository awardRepository) {
        this.awardRepository = awardRepository;
    }

    @Override
    public void accept(List<Award> awards) {
        awards.stream().parallel().forEach(award -> {
            awardRepository.save(ir.jibit.directdebit.gateway.balejbbot.data.entities.Award.builder()
                    .id(UUID.randomUUID().toString())
                    .code(award.getCode())
                    .name(award.getName())
                    .description(award.getDescription())
                    .requiredScore(award.getRequiredScore())
                    .build());
        });
    }

    @Override
    public boolean isAllowed(Role role) {
        return role.getPermissions().stream().anyMatch(INSERT_AWARDS::equals);
    }
}
