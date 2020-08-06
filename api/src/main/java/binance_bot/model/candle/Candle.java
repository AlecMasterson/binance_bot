package binance_bot.model.candle;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.util.Date;

@Entity
@IdClass(CandleId.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Candle implements Serializable {

    @Id
    private String ticker;

    @Id
    private String timePeriod;

    @Id
    private Date endTime;

    private double openPrice;
    private double highPrice;
    private double lowPrice;
    private double closePrice;

    private int trades;
    private double volume;
}
