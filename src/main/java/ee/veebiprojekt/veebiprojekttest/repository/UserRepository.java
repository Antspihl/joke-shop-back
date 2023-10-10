package ee.veebiprojekt.veebiprojekttest.repository;

import ee.veebiprojekt.veebiprojekttest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
