package binance_bot.util;

import binance_bot.exception.AppException;
import binance_bot.model.ta.TAStrategy;
import binance_bot.model.ta.TAConfig;
import binance_bot.model.ta.TAField;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.Rule;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.VolumeIndicator;
import org.ta4j.core.num.DoubleNum;
import org.ta4j.core.num.Num;
import org.ta4j.core.trading.rules.*;

import java.util.*;
import java.util.stream.Collectors;

public class TABuilder {

    private static final String CANDLE_CLOSE_PRICE = "CANDLE_CLOSE_PRICE";

    private static final String BAR_COUNT = "BAR_COUNT";
    private static final String GAIN_PERCENTAGE = "GAIN_PERCENTAGE";
    private static final String INDICATOR = "INDICATOR";
    private static final String INDICATOR_1 = "INDICATOR_1";
    private static final String INDICATOR_2 = "INDICATOR_2";
    private static final String INDICATOR_NUM = "INDICATOR_NUM";
    private static final String LONG_BAR_COUNT = "LONG_BAR_COUNT";
    private static final String LOSS_PERCENTAGE = "LOSS_PERCENTAGE";
    private static final String NTH_PREVIOUS = "NTH_PREVIOUS";
    private static final String MAX_SLOPE = "MAX_SLOPE";
    private static final String MIN_SLOPE = "MIN_SLOPE";
    private static final String MIN_STRENGTH = "MIN_STRENGTH";
    private static final String RULE = "RULE";
    private static final String RULE_1 = "RULE_1";
    private static final String RULE_2 = "RULE_2";
    private static final String SHORT_BAR_COUNT = "SHORT_BAR_COUNT";
    private static final String THRESHOLD = "THRESHOLD";

    public static TAStrategy buildStrategy(List<TAConfig> configs, BarSeries barSeries) {
        Map<String, Indicator<Num>> builtIndicators = new HashMap<>();
        Map<String, Rule> builtRules = new HashMap<>();

        int previousConfigCount = configs.size();
        while (configs.size() > 0) {
            Iterator<TAConfig> configIterator = configs.iterator();

            while (configIterator.hasNext()) {
                TAConfig config = configIterator.next();

                boolean allFieldsValid = config.getFields().stream()
                        .allMatch(field -> {
                            switch (config.getBaseType()) {
                                case INDICATOR:
                                    return !field.getType().equals(INDICATOR_NUM) || builtIndicators.containsKey(field.getValue().toString());
                                case RULE:
                                    return (!field.getType().equals(RULE) || builtRules.containsKey(field.getValue().toString()) &&
                                            (!field.getType().equals(INDICATOR_NUM) || builtIndicators.containsKey(field.getValue().toString())));
                                default:
                                    throw new AppException("Unrecognized Config Type: " + config.getType());
                            }
                        });

                if (allFieldsValid) {
                    switch (config.getBaseType()) {
                        case INDICATOR:
                            builtIndicators.put(config.getName(), buildIndicatorNum(config, barSeries, builtIndicators));
                            break;
                        case RULE:
                            builtRules.put(config.getName(), buildRule(config, builtIndicators, builtRules));
                            break;
                        default:
                            throw new AppException("Unrecognized Config Type: " + config.getType());
                    }

                    configIterator.remove();
                }
            }

            if (previousConfigCount == configs.size()) {
                throw new AppException("Infinite Loop Detected Creating Indicator");
            }

            previousConfigCount = configs.size();
        }

        return TAStrategy.builder().indicators(builtIndicators).rules(builtRules).build();
    }

    public static Indicator<Num> buildIndicatorNum(TAConfig config, BarSeries barSeries, Map<String, Indicator<Num>> existingIndicators) {
        switch (config.getType()) {
            case CANDLE_CLOSE_PRICE:
                return new ClosePriceIndicator(barSeries);
            case "CANDLE_VOLUME_V1":
                return new VolumeIndicator(barSeries);
            case "CANDLE_VOLUME_V2":
                return new VolumeIndicator(barSeries, getIntegerField(config, BAR_COUNT));
            case "MACD_V1":
                return new MACDIndicator(existingIndicators.get(getStringValue(config, INDICATOR)));
            case "MACD_V2":
                return new MACDIndicator(existingIndicators.get(getStringValue(config, INDICATOR)), getIntegerField(config, SHORT_BAR_COUNT), getIntegerField(config, LONG_BAR_COUNT));
            case "RSI":
                return new RSIIndicator(existingIndicators.get(getStringValue(config, INDICATOR)), getIntegerField(config, BAR_COUNT));
            case "SMA":
                return new SMAIndicator(existingIndicators.get(getStringValue(config, INDICATOR)), getIntegerField(config, BAR_COUNT));
            default:
                throw new AppException("No Matching Indicator Found");
        }
    }

    public static Rule buildRule(TAConfig config, Map<String, Indicator<Num>> indicators, Map<String, Rule> existingRules) {
        switch (config.getType()) {
            case "AND":
                return new AndRule(existingRules.get(getStringValue(config, RULE_1)), existingRules.get(getStringValue(config, RULE_2)));
            case "CROSS_OVER_V1":
                return new CrossedUpIndicatorRule(indicators.get(getStringValue(config, INDICATOR)), getDoubleField(config, THRESHOLD));
            case "CROSS_OVER_V2":
                return new CrossedUpIndicatorRule(indicators.get(getStringValue(config, INDICATOR_1)), indicators.get(getStringValue(config, INDICATOR_2)));
            case "CROSS_UNDER_V1":
                return new CrossedDownIndicatorRule(indicators.get(getStringValue(config, INDICATOR)), getDoubleField(config, THRESHOLD));
            case "CROSS_UNDER_V2":
                return new CrossedDownIndicatorRule(indicators.get(getStringValue(config, INDICATOR_1)), indicators.get(getStringValue(config, INDICATOR_2)));
            case "IN_SLOPE":
                return new InSlopeRule(indicators.get(getStringValue(config, INDICATOR)), getIntegerField(config, NTH_PREVIOUS), DoubleNum.valueOf(getDoubleField(config, MIN_SLOPE)), DoubleNum.valueOf(getDoubleField(config, MAX_SLOPE)));
            case "IS_FALLING_V1":
                return new IsFallingRule(indicators.get(getStringValue(config, INDICATOR)), getIntegerField(config, BAR_COUNT));
            case "IS_FALLING_V2":
                return new IsFallingRule(indicators.get(getStringValue(config, INDICATOR)), getIntegerField(config, BAR_COUNT), getDoubleField(config, MIN_STRENGTH));
            case "IS_RISING_V1":
                return new IsRisingRule(indicators.get(getStringValue(config, INDICATOR)), getIntegerField(config, BAR_COUNT));
            case "IS_RISING_V2":
                return new IsRisingRule(indicators.get(getStringValue(config, INDICATOR)), getIntegerField(config, BAR_COUNT), getDoubleField(config, MIN_STRENGTH));
            case "NOT":
                return new NotRule(existingRules.get(getStringValue(config, RULE)));
            case "OR":
                return new OrRule(existingRules.get(getStringValue(config, RULE_1)), existingRules.get(getStringValue(config, RULE_2)));
            case "STOP_GAIN":
                return new StopGainRule((ClosePriceIndicator) indicators.get(CANDLE_CLOSE_PRICE), getDoubleField(config, GAIN_PERCENTAGE));
            case "STOP_LOSS":
                return new StopLossRule((ClosePriceIndicator) indicators.get(CANDLE_CLOSE_PRICE), getDoubleField(config, LOSS_PERCENTAGE));
            default:
                throw new AppException("No Matching Rule Found");
        }
    }

    private static String getStringValue(TAConfig config, String fieldName) {
        return getFieldValue(config, fieldName).toString();
    }

    private static int getIntegerField(TAConfig config, String fieldName) {
        return Integer.parseInt(getFieldValue(config, fieldName).toString());
    }

    private static double getDoubleField(TAConfig config, String fieldName) {
        return Double.parseDouble(getFieldValue(config, fieldName).toString());
    }

    private static Object getFieldValue(TAConfig config, String fieldName) {
        return getField(config.getFields(), fieldName).getValue();
    }

    private static TAField getField(List<TAField> fields, String fieldName) {
        List<TAField> filteredFields = fields.stream()
                .filter(field -> field.getName().equals(fieldName))
                .collect(Collectors.toList());

        if (filteredFields.size() != 1) {
            throw new AppException("Failed to Find Field '" + fieldName + "'. " + filteredFields.size() + " Occurrences Found");
        }

        return filteredFields.get(0);
    }
}
