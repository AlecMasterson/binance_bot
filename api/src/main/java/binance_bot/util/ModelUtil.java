package binance_bot.util;

import com.binance.api.client.domain.market.Candlestick;
import binance_bot.model.candle.Candle;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;
import org.ta4j.core.num.DoubleNum;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class ModelUtil {

    public static Candle candlestickToCandle(String ticker, String timePeriod, Candlestick candle) {
        return Candle.builder()
                .ticker(ticker)
                .timePeriod(timePeriod)
                .endTime(new Date(candle.getCloseTime()))
                .openPrice(Double.parseDouble(candle.getOpen()))
                .highPrice(Double.parseDouble(candle.getHigh()))
                .lowPrice(Double.parseDouble(candle.getLow()))
                .closePrice(Double.parseDouble(candle.getClose()))
                .trades(Math.toIntExact(candle.getNumberOfTrades()))
                .volume(Double.parseDouble(candle.getVolume()))
                .build();
    }

    public static Bar candleToBar(Candle candle) {
        return BaseBar.builder()
                .endTime(ZonedDateTime.ofInstant(candle.getEndTime().toInstant(), ZoneId.systemDefault()))
                .timePeriod(timePeriodToDuration(candle.getTimePeriod()))
                .openPrice(DoubleNum.valueOf(candle.getOpenPrice()))
                .highPrice(DoubleNum.valueOf(candle.getHighPrice()))
                .lowPrice(DoubleNum.valueOf(candle.getLowPrice()))
                .closePrice(DoubleNum.valueOf(candle.getClosePrice()))
                .trades(candle.getTrades())
                .volume(DoubleNum.valueOf(candle.getVolume()))
                .build();
    }

    private static Duration timePeriodToDuration(String timePeriod) {
        switch (timePeriod) {
            case "ONE_MINUTE":
                return Duration.ofMinutes(1);
            case "THREE_MINUTES":
                return Duration.ofMinutes(3);
            case "FIVE_MINUTES":
                return Duration.ofMinutes(5);
            case "FIFTEEN_MINUTES":
                return Duration.ofMinutes(15);
            case "HALF_HOURLY":
                return Duration.ofMinutes(30);
            case "HOURLY":
                return Duration.ofHours(1);
            case "TWO_HOURLY":
                return Duration.ofHours(2);
            case "FOUR_HOURLY":
                return Duration.ofHours(4);
            case "SIX_HOURLY":
                return Duration.ofHours(6);
            case "EIGHT_HOURLY":
                return Duration.ofHours(8);
            case "TWELVE_HOURLY":
                return Duration.ofHours(12);
            case "DAILY":
                return Duration.ofDays(1);
            case "THREE_DAILY":
                return Duration.ofDays(3);
            case "WEEKLY":
                return Duration.ofDays(7);
            case "MONTHLY":
                return Duration.ofDays(30);
            default:
                return null;
        }
    }
}
