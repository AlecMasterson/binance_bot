package binance_bot.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "POSITIONS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Position implements Serializable {

    @Id
    private String id;

    @Column(name = "POS_STATUS")
    private String status;

    private String baseCurrency;
    private String destCurrency;

    private double amount;

    private double buyPrice;
    private Date buyTime;

    private double sellPrice;
    private Date sellTime;
}
