package ee.veebiprojekt.veebiprojekttest.controller;

import ee.veebiprojekt.veebiprojekttest.dto.RatingDTO;
import ee.veebiprojekt.veebiprojekttest.entity.Rating;
import ee.veebiprojekt.veebiprojekttest.service.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
@Slf4j
public class RatingController {
    private final RatingService ratingService;

    @PostMapping
    public RatingDTO addRating(@RequestBody RatingDTO ratingDTO, Principal principal) {
        log.debug("REST request to add rating: {}", ratingDTO);
        return ratingService.addRating(ratingDTO, principal.getName());
    }

    @GetMapping("/{jokeId}")
    public double getJokeRating(@PathVariable Long jokeId) {
        log.debug("REST request to get joke rating: {}", jokeId);
        return ratingService.getJokeRating(jokeId);
    }

    @GetMapping
    public List<Rating> getRatings() {
        log.debug("REST request to get all ratings");
        return ratingService.getRatings();
    }

    @DeleteMapping("/{id}")
    public RatingDTO deleteRating(@PathVariable Long id, Principal principal) {
        log.debug("REST request to delete rating: {}", id);
        return ratingService.deleteRating(id, principal.getName());
    }
}
