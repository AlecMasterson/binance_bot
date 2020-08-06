package binance_bot.model.chart;

import binance_bot.model.candle.Candle;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ChartDataResponse {
    private final List<Candle> candles;
    private final ConfigData configData;
}
