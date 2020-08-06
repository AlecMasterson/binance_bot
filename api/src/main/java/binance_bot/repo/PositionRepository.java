package binance_bot.repo;

import binance_bot.model.Position;
import org.springframework.data.repository.CrudRepository;

public interface PositionRepository extends CrudRepository<Position, String> {
}
