package ee.veebiprojekt.veebiprojekttest.controller;

import ee.veebiprojekt.veebiprojekttest.repository.Rating;
import ee.veebiprojekt.veebiprojekttest.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ee.veebiprojekt.veebiprojekttest.repository.Joke;
import ee.veebiprojekt.veebiprojekttest.repository.JokeRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/jokes")
@RequiredArgsConstructor
public class Controller {
    private final JokeRepository jokeRepository;
    private final RatingRepository ratingRepository;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    //TODO change to POST
    //TODO replace path variables with JSON {setup: "setup", punchline: "punchline"}
    @GetMapping("/setup/{setup}/punchline/{punchline}")
    public String addJoke(@PathVariable("setup") String setup, @PathVariable("punchline") String punchline) {
        Joke newJoke = new Joke();
        newJoke.setSetup(setup);
        newJoke.setPunchline(punchline);
        jokeRepository.save(newJoke);
        return String.format("%s %s", setup, punchline);
    }

    @GetMapping()
    public List<Joke> getAllJokes() {
        return jokeRepository.findAll();
    }

    @GetMapping("/ratings")
    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    @GetMapping("/ratings/{rating}/product/{productId}")
    public String addRating(@PathVariable("rating") int rating, @PathVariable("productId") Long productId) {
        if (jokeRepository.findById(productId).isEmpty()) {
            return "No such product";
        }
        if (rating < 1 || rating > 5) {
            return "Rating must be between 1 and 5";
        }
        Rating newRating = new Rating();
        newRating.setRatingValue(rating);
        newRating.setProductId(productId);
        ratingRepository.save(newRating);
        return String.format("%s %s", rating, productId);
    }

    @GetMapping("/ratings/{productId}")
    public double getRating(@PathVariable("productId") Long productId) {
        List<Rating> ratings = ratingRepository.findAll();
        double sum = 0;
        int count = 0;
        for (Rating rating : ratings) {
            if (Objects.equals(rating.getProductId(), productId)) {
                sum += rating.getRatingValue();
                count++;
            }
        }
        if (count == 0) {
            return 0;
        }
        double rating = sum / count;
        rating = Math.round(rating * 10) / 10.0;
        return rating;
    }

    @GetMapping("/get/{id}")
    public Optional<Joke> getJokeById(@PathVariable("id") long id) {
        return jokeRepository.findById(id);
    }

    @GetMapping("/delete/{id}")
    public void removeJokeById(@PathVariable("id") long id) {
        jokeRepository.deleteById(id);
    }
}
