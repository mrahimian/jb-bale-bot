package ir.jibit.directdebit.gateway.balejbbot.data.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "gift_time")
public class GiftTime {
    @Id
    private int id = 0;
    private boolean isActive;
}
