package pl.strumnik.hms.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.strumnik.hms.domain.House;

import java.util.Set;

public interface HouseRepository extends CrudRepository<House, Long> {

    @EntityGraph(attributePaths = {"inhabitants", "houseworks"})
    @Query("SELECT h FROM House h WHERE h.owner.id = :ownerId")
    Set<House> findAllByOwnerId(Long ownerId);

    @EntityGraph(attributePaths = {"inhabitants", "houseworks"})
    @Query("SELECT h FROM House h LEFT JOIN h.inhabitants i WHERE i.id = :inhabitantId")
    Set<House> findAllByInhabitantId(Long inhabitantId);
}
