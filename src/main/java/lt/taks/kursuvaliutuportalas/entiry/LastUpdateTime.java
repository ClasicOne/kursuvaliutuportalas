package lt.taks.kursuvaliutuportalas.entiry;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;

@Data
@Entity
public class LastUpdateTime {
    @Id
            @GeneratedValue
    Long id;
    Date date;

    public LastUpdateTime(Date date) {
        this.date = date;
    }

    public LastUpdateTime() {
    }
}
