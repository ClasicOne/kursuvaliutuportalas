package lt.taks.kursuvaliutuportalas.entiry;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class CurrencyTimeline {
    @Id @GeneratedValue
            private Long currencyTimeline_id;

    String code, tp;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date time;
    float value;

}
