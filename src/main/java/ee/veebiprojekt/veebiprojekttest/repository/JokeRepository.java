package ee.veebiprojekt.veebiprojekttest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JokeRepository extends JpaRepository<Joke, Long> {

}
