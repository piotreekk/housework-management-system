package pl.strumnik.hms.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.strumnik.hms.domain.HouseInhabitant;

import java.util.Optional;
import java.util.Set;

public interface HouseInhabitantRepository extends CrudRepository<HouseInhabitant, Long> {

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT i FROM HouseInhabitant i WHERE i.id = :id")
    Optional<HouseInhabitant> findByIdWithUser(Long id);

    @EntityGraph(attributePaths = {"user", "house"})
    @Query("SELECT i FROM HouseInhabitant i WHERE i.house.id = :houseId")
    Set<HouseInhabitant> findAllByHouseId(Long houseId);
}
