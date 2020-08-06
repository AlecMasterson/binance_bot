package binance_bot.service;

import binance_bot.config.AppProperties;
import binance_bot.model.candle.CandleRequest;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.CandlestickInterval;
import binance_bot.model.candle.Candle;
import binance_bot.repo.CandleRepository;
import binance_bot.util.ModelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ta4j.core.Bar;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TickerService {

    private final AppProperties appProperties;
    private final BinanceApiRestClient client;
    private final CandleRepository repository;

    @Autowired
    public TickerService(AppProperties appProperties, BinanceApiRestClient client, CandleRepository repository) {
        this.appProperties = appProperties;
        this.client = client;
        this.repository = repository;
    }

    public List<Candle> getCandles(CandleRequest request) {
        try {
            return repository.findByTickerAndTimePeriod(request.getTicker(), request.getInterval());
        } catch (Exception e) {
            log.error("[{}] - Failed to Acquire Data on Interval {}", request.getTicker(), request.getInterval(), e);
            return Collections.emptyList();
        }
    }

    public List<String> getTickers() {
        return appProperties.getTickers();
    }

    public List<String> getIntervals() {
        return Arrays.stream(CandlestickInterval.values())
                .map(CandlestickInterval::name)
                .collect(Collectors.toList());
    }

    public void updateTicker(String ticker, CandlestickInterval interval) {
        try {
            List<Candle> candles = client.getCandlestickBars(ticker, interval).stream()
                    .map(candle -> ModelUtil.candlestickToCandle(ticker, interval.name(), candle))
                    .collect(Collectors.toList());

            repository.saveAll(candles);
        } catch (Exception e) {
            log.error("[{}] - Failed to Update on Interval {}", ticker, interval, e);
        }
    }

    public List<Bar> getTickerBars(String ticker, CandlestickInterval interval) {
        try {
            CandleRequest request = CandleRequest.builder()
                    .ticker(ticker)
                    .interval(interval.name())
                    .build();

            return getCandles(request).stream()
                .map(ModelUtil::candleToBar)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[{}] - Failed to Acquire Data on Interval {}", ticker, interval, e);
            return Collections.emptyList();
        }
    }
}
