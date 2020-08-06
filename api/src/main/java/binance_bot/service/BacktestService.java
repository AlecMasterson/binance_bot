package binance_bot.service;

import binance_bot.component.Backtest;
import binance_bot.model.backtest.BacktestRequest;
import binance_bot.model.backtest.BacktestResponse;
import binance_bot.model.backtest.BacktestResult;
import binance_bot.model.candle.Candle;
import binance_bot.model.candle.CandleRequest;
import binance_bot.model.chart.ChartDataRequest;
import binance_bot.model.chart.ChartDataResponse;
import binance_bot.model.chart.ConfigData;
import binance_bot.model.ta.TAConfig;
import binance_bot.util.JsonUtil;
import binance_bot.util.ModelUtil;
import binance_bot.util.TABuilder;
import com.binance.api.client.domain.market.CandlestickInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ta4j.core.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BacktestService {

    private static final String INDICATOR_CONFIGS = "/configs/indicator_configs.json";
    private static final String RULE_CONFIGS = "/configs/rule_configs.json";

    private final TickerService tickerService;
    private final Backtest backtest;

    @Autowired
    public BacktestService(TickerService tickerService, Backtest backtest) {
        this.tickerService = tickerService;
        this.backtest = backtest;
    }

    public List<TAConfig> getIndicatorConfigs() {
        return Arrays.asList(JsonUtil.readResource(INDICATOR_CONFIGS, TAConfig[].class));
    }

    public List<TAConfig> getRuleConfigs() {
        return Arrays.asList(JsonUtil.readResource(RULE_CONFIGS, TAConfig[].class));
    }

    public BacktestResponse runBacktest(BacktestRequest request) {
        Map<String, BacktestResult> results = request.getTickers().parallelStream()
                .collect(Collectors.toMap(String::toString, ticker -> {
                    List<Bar> bars = tickerService.getTickerBars(ticker, CandlestickInterval.valueOf(request.getTimePeriod()));
                    BarSeries barSeries = new BaseBarSeriesBuilder().withName(ticker).withBars(bars).build();

                    return backtest.runBacktest(request, barSeries);
                }));

        return BacktestResponse.builder().results(results).build();
    }

    public ChartDataResponse getChartData(ChartDataRequest request) {
        List<Candle> candles = tickerService.getCandles(CandleRequest.builder()
                .ticker(request.getTicker())
                .interval(request.getTimePeriod())
                .build());
        BarSeries barSeries = new BaseBarSeriesBuilder()
                .withName(request.getTicker())
                .withBars(candles.stream().map(ModelUtil::candleToBar).collect(Collectors.toList()))
                .build();

        Map<String, List<Double>> configValues = TABuilder.buildStrategy(new ArrayList<>(request.getConfigs()), barSeries)
                .getIndicators().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> IntStream.range(0, candles.size())
                                .mapToObj(i -> entry.getValue().getValue(i).doubleValue())
                                .collect(Collectors.toList())));

        return ChartDataResponse.builder()
                .candles(candles)
                .configData(ConfigData.builder().configValues(configValues).build())
                .build();
    }
}
