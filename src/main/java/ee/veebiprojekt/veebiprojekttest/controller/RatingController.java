package ee.veebiprojekt.veebiprojekttest.controller;

import ee.veebiprojekt.veebiprojekttest.dto.RatingDTO;
import ee.veebiprojekt.veebiprojekttest.entity.Rating;
import ee.veebiprojekt.veebiprojekttest.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @PostMapping
    public RatingDTO addRating(@RequestBody RatingDTO ratingDTO) {
        return ratingService.addRating(ratingDTO);
    }

    @GetMapping("/{jokeId}")
    public double getJokeRating(@PathVariable Long jokeId) {
        return ratingService.getJokeRating(jokeId);
    }

    @GetMapping
    public List<Rating> getRatings() {
        return ratingService.getRatings();
    }

    @PostMapping("/{id}")
    public RatingDTO editRating(@RequestBody RatingDTO ratingDTO, @PathVariable Long id) {
        return ratingService.editRating(ratingDTO, id);
    }

    @DeleteMapping("/{id}")
    public RatingDTO deleteRating(@PathVariable Long id) {
        return ratingService.deleteRating(id);
    }
}
