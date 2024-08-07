package ir.jibit.directdebit.gateway.balejbbot.service.models.admins;

public record UpdateScoreModel (String[] studentIds, int score, boolean increase){
}
