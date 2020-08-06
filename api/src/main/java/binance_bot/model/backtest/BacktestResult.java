package binance_bot.model.backtest;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BacktestResult {
    private int numTrades;
    private double profit;
}
