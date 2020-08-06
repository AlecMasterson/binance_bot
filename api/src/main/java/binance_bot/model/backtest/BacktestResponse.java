package binance_bot.model.backtest;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class BacktestResponse {
    private final Map<String, BacktestResult> results;
}
