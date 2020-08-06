package binance_bot.component;

import binance_bot.model.ta.TAStrategy;
import binance_bot.model.backtest.BacktestRequest;
import binance_bot.model.backtest.BacktestResult;
import binance_bot.util.TABuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ta4j.core.*;
import org.ta4j.core.analysis.criteria.TotalProfitCriterion;
import org.ta4j.core.num.Num;

import java.util.ArrayList;

@Component
@Slf4j
public class Backtest {

    public BacktestResult runBacktest(BacktestRequest config, BarSeries barSeries) {
        TAStrategy configs = TABuilder.buildStrategy(new ArrayList<>(config.getConfigs()), barSeries);

        Strategy strategy = new BaseStrategy(configs.getRules().get(config.getEntryRule()), configs.getRules().get(config.getExitRule()), config.getUnstablePeriod());
        TradingRecord tradingRecord = new BarSeriesManager(barSeries).run(strategy);

        AnalysisCriterion criterion = new TotalProfitCriterion();
        Num profit = criterion.calculate(barSeries, tradingRecord);

        log.info("[{}] - Number of Trades = {}", barSeries.getName(), tradingRecord.getTradeCount());
        log.info("[{}] - Profit = {}", barSeries.getName(), profit);

        return BacktestResult.builder()
                .numTrades(tradingRecord.getTradeCount())
                .profit(profit.doubleValue())
                .build();
    }
}
