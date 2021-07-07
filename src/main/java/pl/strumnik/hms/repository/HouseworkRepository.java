package pl.strumnik.hms.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.strumnik.hms.domain.Housework;

import java.util.Optional;
import java.util.Set;

public interface HouseworkRepository extends CrudRepository<Housework, Long> {

    Optional<Housework> findById(Long id);

    @EntityGraph(attributePaths = {"assignedUser"})
    @Query("SELECT h FROM Housework h WHERE h.id = :id")
    Optional<Housework> findByIdWithUser(Long id);

    @Query("SELECT h FROM Housework h WHERE h.house.id = :houseId ORDER BY h.scheduledAt DESC")
    Set<Housework> findAllByHouseId_01(Long houseId);
}
