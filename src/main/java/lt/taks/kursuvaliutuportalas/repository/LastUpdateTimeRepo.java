package lt.taks.kursuvaliutuportalas.repository;

import lt.taks.kursuvaliutuportalas.entiry.LastUpdateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface LastUpdateTimeRepo extends JpaRepository<LastUpdateTime, Long> {
 LastUpdateTime findFirstByDate(Date date);
}
