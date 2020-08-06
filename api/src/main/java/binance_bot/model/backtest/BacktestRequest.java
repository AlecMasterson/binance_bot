package binance_bot.model.backtest;

import binance_bot.model.ta.TAConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BacktestRequest {
    private List<TAConfig> configs;
    private String entryRule;
    private String exitRule;
    private List<String> tickers;
    private String timePeriod;
    private int unstablePeriod;
}
