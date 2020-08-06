package binance_bot.callback;

import binance_bot.event.OrderUpdateEvent;
import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.domain.event.OrderTradeUpdateEvent;
import com.binance.api.client.domain.event.UserDataUpdateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class UserDataCallback implements BinanceApiCallback<UserDataUpdateEvent> {

    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public UserDataCallback(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void onResponse(final UserDataUpdateEvent event) {
        log.info("UserDataUpdateEvent Received of Type = {}", event.getEventType());

        switch (event.getEventType()) {
            case ACCOUNT_UPDATE:
                log.warn("AccountUpdate Events Not Supported Yet");
                break;
            case ACCOUNT_POSITION_UPDATE:
                log.warn("AccountPositionUpdate Events Not Supported Yet");
                break;
            case BALANCE_UPDATE:
                log.warn("BalanceUpdate Events Not Supported Yet");
                break;
            case ORDER_TRADE_UPDATE:
                OrderTradeUpdateEvent orderEvent = event.getOrderTradeUpdateEvent();

                // TODO: Determine correct "amount" property.
                eventPublisher.publishEvent(OrderUpdateEvent.builder()
                        .orderId(orderEvent.getOrderId())
                        .orderStatus(orderEvent.getOrderStatus())
                        .orderSide(orderEvent.getSide())
                        .symbol(orderEvent.getSymbol())
                        .amount(Double.parseDouble(orderEvent.getQuoteOrderQty()))
                        .price(Double.parseDouble(orderEvent.getPrice()))
                        .time(new Date(orderEvent.getEventTime()))
                        .build());
                break;
        }
    }

    @Override
    public void onFailure(final Throwable e) {
        log.error("Failure Retrieving UserDataUpdateEvent", e);
    }
}
