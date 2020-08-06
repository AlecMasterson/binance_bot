package binance_bot.config;

import com.binance.api.client.BinanceApiAsyncRestClient;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BinanceClientConfig {

    private final AppProperties appProperties;

    @Autowired
    public BinanceClientConfig(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Bean("asyncClient")
    public BinanceApiAsyncRestClient binanceApiAsyncRestClient() {
        return BinanceApiClientFactory
                .newInstance(appProperties.getBinancePublic(), appProperties.getBinanceSecret())
                .newAsyncRestClient();
    }

    @Bean("restClient")
    public BinanceApiRestClient binanceApiRestClient() {
        return BinanceApiClientFactory
                .newInstance(appProperties.getBinancePublic(), appProperties.getBinanceSecret())
                .newRestClient();
    }

    @Bean("webSocketClient")
    public BinanceApiWebSocketClient binanceApiWebSocketClient() {
        return BinanceApiClientFactory
                .newInstance(appProperties.getBinancePublic(), appProperties.getBinanceSecret())
                .newWebSocketClient();
    }
}
