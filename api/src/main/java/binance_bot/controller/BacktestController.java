package binance_bot.controller;

import binance_bot.model.backtest.BacktestRequest;
import binance_bot.model.backtest.BacktestResponse;
import binance_bot.model.chart.ChartDataRequest;
import binance_bot.model.chart.ChartDataResponse;
import binance_bot.model.ta.TAConfig;
import binance_bot.service.BacktestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/backtest")
public class BacktestController {

    private final BacktestService backtestService;

    @Autowired
    public BacktestController(BacktestService backtestService) {
        this.backtestService = backtestService;
    }

    @RequestMapping(value = "/indicators")
    public List<TAConfig> getIndicatorConfigs() {
        return backtestService.getIndicatorConfigs();
    }

    @RequestMapping(value = "/rules")
    public List<TAConfig> getRuleConfigs() {
        return backtestService.getRuleConfigs();
    }

    @PostMapping(value = "/run")
    public BacktestResponse runBacktest(@RequestBody BacktestRequest request) {
        return backtestService.runBacktest(request);
    }

    @PostMapping(value = "/chart-data")
    public ChartDataResponse getChartData(@RequestBody ChartDataRequest request) {
        return backtestService.getChartData(request);
    }
}
