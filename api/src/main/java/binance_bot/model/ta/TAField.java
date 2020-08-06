package binance_bot.model.ta;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TAField {
    private String description;
    private String name;
    private String type;
    private Object value;
}
