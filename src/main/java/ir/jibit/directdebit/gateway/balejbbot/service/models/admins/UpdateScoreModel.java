package ir.jibit.directdebit.gateway.balejbbot.service.models.admins;

public record UpdateScoreModel (String teacherId, String studentChatId, int score, boolean increase){
}
