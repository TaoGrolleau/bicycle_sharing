package spring.main.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import spring.main.models.BicycleStation;

import java.util.List;

@Repository
public interface BicycleStationRepository extends CrudRepository<BicycleStation, Long> {
    List<BicycleStation> findByCity(String city);
}
