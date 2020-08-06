package binance_bot.model.candle;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CandleRequest {
    private String interval;
    private String ticker;
}
