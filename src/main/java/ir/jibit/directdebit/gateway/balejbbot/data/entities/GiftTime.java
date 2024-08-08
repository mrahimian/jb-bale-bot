package ir.jibit.directdebit.gateway.balejbbot.data.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "gift_time")
public class GiftTime {
    @Id
    private String id;
    private boolean isActive;
}
