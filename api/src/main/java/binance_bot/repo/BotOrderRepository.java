package binance_bot.repo;

import binance_bot.model.BotOrder;
import com.binance.api.client.domain.OrderStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BotOrderRepository extends CrudRepository<BotOrder, Long> {
    boolean existsByOrderIdEquals(Long orderId);
    List<BotOrder> findAllByOrderStatusEqualsOrOrderStatusEquals(OrderStatus orderStatus1, OrderStatus orderStatus2);
}
