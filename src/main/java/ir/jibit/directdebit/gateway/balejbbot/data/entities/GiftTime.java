package ir.jibit.directdebit.gateway.balejbbot.data.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "gift_time")
public class GiftTime {
    @Id
    private int id;
    private boolean isActive;

    public GiftTime() {

    }
}
