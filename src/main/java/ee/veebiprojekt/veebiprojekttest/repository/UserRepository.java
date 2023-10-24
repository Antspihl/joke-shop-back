package ee.veebiprojekt.veebiprojekttest.repository;

import ee.veebiprojekt.veebiprojekttest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT e FROM User e WHERE e.username = :username")
    User findByUsername(String username);

    @Query("SELECT e FROM User e WHERE e.email = :email")
    User findByEmail(String email);
}