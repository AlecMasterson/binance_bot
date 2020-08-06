package binance_bot.component;

import binance_bot.config.AppProperties;
import binance_bot.service.TickerService;
import com.binance.api.client.domain.market.CandlestickInterval;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class InitialData {

    private final AppProperties appProperties;
    private final TickerService tickerService;

    private final List<CandlestickInterval> timePeriods;

    @Autowired
    public InitialData(AppProperties appProperties, TickerService tickerService) {
        this.appProperties = appProperties;
        this.tickerService = tickerService;

        this.timePeriods = Arrays.asList(CandlestickInterval.values());
    }

    //@EventListener(ApplicationReadyEvent.class)
    public void init() {
        log.info("Initializing DB with Data");

        appProperties.getTickers().forEach(ticker -> {
            CompletableFuture.runAsync(() -> {
                timePeriods.forEach(timePeriod -> tickerService.updateTicker(ticker, timePeriod));
            });
        });
    }
}
