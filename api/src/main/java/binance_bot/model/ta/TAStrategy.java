package binance_bot.model.ta;

import lombok.Builder;
import lombok.Getter;
import org.ta4j.core.Indicator;
import org.ta4j.core.Rule;
import org.ta4j.core.num.Num;

import java.util.Map;

@Builder
@Getter
public class TAStrategy {
    private final Map<String, Indicator<Num>> indicators;
    private final Map<String, Rule> rules;
}
