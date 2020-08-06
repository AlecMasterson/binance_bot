package binance_bot.model.chart;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Builder
@Getter
public class ConfigData {
    private final Map<String, List<Double>> configValues;
}
