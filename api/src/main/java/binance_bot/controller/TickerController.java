package binance_bot.controller;

import binance_bot.model.candle.Candle;
import binance_bot.model.candle.CandleRequest;
import binance_bot.service.TickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/ticker")
public class TickerController {

    private final TickerService tickerService;

    @Autowired
    public TickerController(TickerService tickerService) {
        this.tickerService = tickerService;
    }

    @PostMapping(value = "/candles")
    public List<Candle> getCandles(@RequestBody CandleRequest request) {
        return tickerService.getCandles(request);
    }

    @RequestMapping(value = "/tickers")
    public List<String> getTickers() {
        return tickerService.getTickers();
    }

    @RequestMapping(value = "/intervals")
    public List<String> getIntervals() {
        return tickerService.getIntervals();
    }
}
