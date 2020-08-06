package binance_bot.model.chart;

import binance_bot.model.ta.TAConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChartDataRequest {
    private List<TAConfig> configs;
    private String ticker;
    private String timePeriod;
}
