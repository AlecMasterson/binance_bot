package binance_bot.model;

import com.binance.api.client.domain.OrderStatus;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity(name = "BOT_ORDER")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BotOrder implements Serializable {

    @Id
    private Long orderId;
    private String symbol;
    private OrderStatus orderStatus;
}
