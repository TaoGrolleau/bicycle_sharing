package spring.main.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import spring.main.models.BicycleStation;

@Repository
public interface BicycleStationRepository extends CrudRepository<BicycleStation, Long> {
}
