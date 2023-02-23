package uz.md.shopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.md.shopapp.domain.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
