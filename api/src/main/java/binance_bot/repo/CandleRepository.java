package binance_bot.repo;

import binance_bot.model.candle.Candle;
import binance_bot.model.candle.CandleId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandleRepository extends CrudRepository<Candle, CandleId> {
    List<Candle> findByTickerAndTimePeriod(String ticker, String timePeriod);
}
