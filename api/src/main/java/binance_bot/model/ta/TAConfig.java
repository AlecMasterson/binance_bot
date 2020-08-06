package binance_bot.model.ta;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TAConfig {
    private String baseType;
    private String description;
    private List<TAField> fields;
    private String name;
    private String type;
}
