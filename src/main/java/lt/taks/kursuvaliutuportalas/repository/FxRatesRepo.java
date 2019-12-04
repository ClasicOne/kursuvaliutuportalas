package lt.taks.kursuvaliutuportalas.repository;

import lt.taks.kursuvaliutuportalas.entiry.FxRates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FxRatesRepo extends JpaRepository<FxRates, Long> {
}
