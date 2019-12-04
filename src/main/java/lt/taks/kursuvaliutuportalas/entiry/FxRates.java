package lt.taks.kursuvaliutuportalas.entiry;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Entity
@Data
public class FxRates {

    @Id
    @GeneratedValue
    Long fxRates_id;

    Date date;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ccyAnt_id")
    List<CcyAmt> ccyAmt = new ArrayList<>();

    String  tp;
}
