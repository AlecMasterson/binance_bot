package binance_bot.listener;

import binance_bot.event.OrderUpdateEvent;
import binance_bot.service.PositionService;
import com.binance.api.client.domain.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderUpdateListener {

    private final PositionService positionService;

    @Autowired
    public OrderUpdateListener(PositionService positionService) {
        this.positionService = positionService;
    }

    @Async
    @EventListener
    public void handleOrderUpdateEvent(OrderUpdateEvent event) {
        if (event.getOrderStatus().equals(OrderStatus.FILLED)) {
            log.info("OrderID {} Has Been Filled to {}", event.getOrderId(), event.getOrderSide());

            switch (event.getOrderSide()) {
                case BUY:
                    positionService.createPosition(event);
                    break;
                case SELL:
                    positionService.closePosition(event);
                    break;
            }
        }
    }
}
