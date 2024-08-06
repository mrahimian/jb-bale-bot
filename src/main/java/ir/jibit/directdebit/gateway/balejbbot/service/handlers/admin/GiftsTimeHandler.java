package ir.jibit.directdebit.gateway.balejbbot.service.handlers.admin;

import ir.jibit.directdebit.gateway.balejbbot.data.GiftTimeRepository;
import ir.jibit.directdebit.gateway.balejbbot.service.handlers.AdminConsumerHandler;
import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Role;

import static ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Permission.UPDATE_GIFTS_TIME;

public class GiftsTimeHandler implements AdminConsumerHandler<Boolean> {
    private final GiftTimeRepository giftTimeRepository;

    public GiftsTimeHandler(GiftTimeRepository giftTimeRepository) {
        this.giftTimeRepository = giftTimeRepository;
    }

    @Override
    public void accept(Boolean enable) {
        var gTime = giftTimeRepository.getReferenceById(0L);
        gTime.setActive(enable);
        giftTimeRepository.save(gTime);
    }

    @Override
    public boolean isAllowed(Role role) {
        return role.getPermissions().stream().anyMatch(UPDATE_GIFTS_TIME::equals);
    }
}
