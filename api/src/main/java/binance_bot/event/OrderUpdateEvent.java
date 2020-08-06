package binance_bot.event;

import com.binance.api.client.domain.OrderSide;
import com.binance.api.client.domain.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class OrderUpdateEvent {
    private final Long orderId;
    private final OrderStatus orderStatus;
    private final OrderSide orderSide;
    private final String symbol;
    private final double amount;
    private final double price;
    private final Date time;
}
