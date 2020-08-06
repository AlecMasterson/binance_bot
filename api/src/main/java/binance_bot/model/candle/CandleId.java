package binance_bot.model.candle;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CandleId implements Serializable {

    private String ticker;
    private String timePeriod;
    private Date endTime;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof CandleId)) {
            return false;
        }

        CandleId other = (CandleId) obj;

        return this.getTicker().equals(other.ticker) &&
                this.getTimePeriod().equals(other.getTimePeriod()) &&
                this.getEndTime().equals(other.getEndTime());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
