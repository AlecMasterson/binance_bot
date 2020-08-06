package binance_bot.util;

import binance_bot.exception.AppException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class JsonUtil {

    public static <T> T readResource(String fileName, Class<T> entityType) {
        try (InputStream configFileStream = JsonUtil.class.getResourceAsStream(fileName)) {
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType type = objectMapper.getTypeFactory().constructType(entityType);

            return objectMapper.readValue(new InputStreamReader(configFileStream, StandardCharsets.UTF_8), type);
        } catch (Exception e) {
            throw new AppException("Failed to Read Resource File: " + fileName, e);
        }
    }

    public static <T> T readLinkedHashMap(Object object, Class<T> entityType) {
        return new ObjectMapper().convertValue(object, entityType);
    }
}
