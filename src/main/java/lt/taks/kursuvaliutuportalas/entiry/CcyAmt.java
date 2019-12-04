package lt.taks.kursuvaliutuportalas.entiry;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Entity
@Data
public class CcyAmt {
    @Id
    @GeneratedValue
    private Long ccyAnt_id;

    String Ccy;
    float Amt;


}
