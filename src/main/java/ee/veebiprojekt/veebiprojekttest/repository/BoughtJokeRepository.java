package ee.veebiprojekt.veebiprojekttest.repository;

import ee.veebiprojekt.veebiprojekttest.entity.BoughtJoke;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoughtJokeRepository extends JpaRepository<BoughtJoke, Long> {

    @Query("SELECT bj.jokeId FROM BoughtJoke bj WHERE bj.userId = :userId")
    List<Long> findJokeIdsByUserId(@Param("userId")Long id);
}
