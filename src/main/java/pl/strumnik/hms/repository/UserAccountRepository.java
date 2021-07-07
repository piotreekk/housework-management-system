package pl.strumnik.hms.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.strumnik.hms.domain.UserAccount;

import java.util.Optional;
import java.util.Set;

public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {

    @EntityGraph(attributePaths = {"roles"})
    @Query("SELECT u FROM UserAccount u WHERE u.login = :login")
    Optional<UserAccount> findByLoginWithRoles(String login);

    @EntityGraph(attributePaths = {"houseworks"})
    @Query("SELECT u FROM UserAccount u WHERE u.id = :id")
    Optional<UserAccount> findByIdWithHouseworks(Long id);

    @Query("SELECT u FROM UserAccount u LEFT JOIN u.houses h " +
            "WHERE h IS NULL " +
            "OR h.house.id <> :houseId " +
            "OR EXISTS (" +
            "   SELECT COUNT(h2) FROM HouseInhabitant h2 WHERE h2.house.id = :houseId AND h2.user.id = h.user.id " +
            "   GROUP BY h2.house, h2.user HAVING MAX(COALESCE(h2.dateTo, CURRENT_DATE + 1)) <= CURRENT_DATE" +
            ")")
    Set<UserAccount> findCandidatesToHouseInhabitants(Long houseId);
}
