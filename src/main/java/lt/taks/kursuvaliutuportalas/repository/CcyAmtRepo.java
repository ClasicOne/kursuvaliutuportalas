package lt.taks.kursuvaliutuportalas.repository;

import lt.taks.kursuvaliutuportalas.entiry.CcyAmt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CcyAmtRepo extends JpaRepository<CcyAmt, Long> {
}
