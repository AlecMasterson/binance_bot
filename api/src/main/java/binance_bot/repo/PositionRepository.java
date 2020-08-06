package binance_bot.repo;

import binance_bot.model.Position;
import binance_bot.model.PositionStatus;
import org.springframework.data.repository.CrudRepository;

public interface PositionRepository extends CrudRepository<Position, String> {
    Position findByStatusAndSymbol(PositionStatus status, String symbol);
}
