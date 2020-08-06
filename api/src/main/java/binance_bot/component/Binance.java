package binance_bot.component;

import binance_bot.callback.CandlestickCallback;
import binance_bot.callback.UserDataCallback;
import binance_bot.model.BotOrder;
import binance_bot.repo.BotOrderRepository;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.OrderStatus;
import com.binance.api.client.domain.account.Order;
import com.binance.api.client.domain.account.request.OrderStatusRequest;
import com.binance.api.client.domain.market.CandlestickInterval;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Slf4j
public class Binance {

    private final BinanceApiRestClient restClient;
    private final BinanceApiWebSocketClient webSocketClient;
    private final UserDataCallback userDataCallback;
    private final CandlestickCallback candlestickCallback;
    private final BotOrderRepository botOrderRepository;

    @Autowired
    public Binance(BinanceApiRestClient restClient, BinanceApiWebSocketClient webSocketClient,
                   UserDataCallback userDataCallback, CandlestickCallback candlestickCallback,
                   BotOrderRepository botOrderRepository) {
        this.restClient = restClient;
        this.webSocketClient = webSocketClient;
        this.userDataCallback = userDataCallback;
        this.candlestickCallback = candlestickCallback;
        this.botOrderRepository = botOrderRepository;
    }

    @PostConstruct
    public void init() {
        String userDataKey = restClient.startUserDataStream();
        restClient.keepAliveUserDataStream(userDataKey);

        // TODO: Determine a way to keepalive or restart WebSockets on failure.
        webSocketClient.onUserDataUpdateEvent(userDataKey, userDataCallback);
        webSocketClient.onCandlestickEvent("bnbbtc,ethbtc", CandlestickInterval.ONE_MINUTE, candlestickCallback);
    }

    @Scheduled(cron = "*/30 * * * * *")
    public void monitorBotOrders() {
        List<BotOrder> orders = botOrderRepository.findAllByOrderStatusEqualsOrOrderStatusEquals(OrderStatus.NEW, OrderStatus.PARTIALLY_FILLED);

        orders.forEach(order -> {
            Order orderStatus = restClient.getOrderStatus(new OrderStatusRequest(order.getSymbol(), order.getOrderId()));
            order.setOrderStatus(orderStatus.getStatus());

            // TODO: Handle orders that are taking too long.

            botOrderRepository.save(order);
        });
    }
}
