package lt.taks.kursuvaliutuportalas.repository;

import lt.taks.kursuvaliutuportalas.entiry.CurrencyTimeline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface CurrencyTimelineRepo extends JpaRepository<CurrencyTimeline, Long> {
   List<CurrencyTimeline> getAllByCode(String code);

}
