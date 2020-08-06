package binance_bot.callback;

import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.domain.event.CandlestickEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CandlestickCallback implements BinanceApiCallback<CandlestickEvent> {

    @Override
    public void onResponse(final CandlestickEvent event) {
        log.info("CandlestickEvent Received of Type = ({},{})", event.getSymbol(), event.getIntervalId());
    }

    @Override
    public void onFailure(final Throwable e) {
        log.error("Failure Retrieving CandlestickEvent", e);
    }
}
