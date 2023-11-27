package ee.veebiprojekt.veebiprojekttest.service;

import ee.veebiprojekt.veebiprojekttest.dto.JokeDTO;
import ee.veebiprojekt.veebiprojekttest.entity.Joke;
import ee.veebiprojekt.veebiprojekttest.entity.Rating;
import ee.veebiprojekt.veebiprojekttest.mapper.JokeMapper;
import ee.veebiprojekt.veebiprojekttest.repository.JokeRepository;
import ee.veebiprojekt.veebiprojekttest.exception.EntityNotFoundException;
import ee.veebiprojekt.veebiprojekttest.repository.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class JokeService {
    private final JokeRepository jokeRepository;
    private final JokeMapper jokeMapper;
    private final RatingRepository ratingRepository;
    private static final String ENTITY_NAME = "joke";

    public JokeService(JokeRepository jokeRepository, JokeMapper jokeMapper, RatingRepository ratingRepository) {
        this.jokeRepository = jokeRepository;
        this.jokeMapper = jokeMapper;
        this.ratingRepository = ratingRepository;
    }

    public JokeDTO addJoke(JokeDTO jokeDTO) {
        Joke joke = new Joke();
        joke.setSetup(jokeDTO.setup());
        joke.setPunchline(jokeDTO.punchline());
        if (jokeDTO.price() != null) joke.setPrice(jokeDTO.price());
        if (jokeDTO.timesBought() != null) joke.setTimesBought(jokeDTO.timesBought());
        jokeRepository.save(joke);
        return jokeMapper.toDTO(joke);
    }

    public JokeDTO getJoke(long id) {
        Joke joke = jokeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, id));
        return jokeMapper.toDTO(joke);
    }

    public List<JokeDTO> getJokes() {
        return jokeMapper.toDTOList(jokeRepository.findAll());
    }

    public JokeDTO editJoke(long id, JokeDTO newJoke) {
        Joke joke = jokeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, id));
        if (newJoke.setup() != null) joke.setSetup(newJoke.setup());
        if (newJoke.punchline() != null) joke.setPunchline(newJoke.punchline());
        if (newJoke.price() != null) joke.setPrice(newJoke.price());
        if (newJoke.timesBought() != null) joke.setTimesBought(newJoke.timesBought());
        jokeRepository.save(joke);
        return jokeMapper.toDTO(joke);
    }

    public void recalculateJokeRating(long id) {
        Joke joke = jokeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, id));
        List<Rating> ratings = ratingRepository.findAllByJokeId(id);
        double newRating = ratings.stream()
                .mapToInt(Rating::getRatingValue)
                .average()
                .orElse(0.0);
        joke.setRating(newRating);
        jokeRepository.save(joke);
    }

    public void deleteJoke(long id) {
        jokeRepository.deleteById(id);
    }

    public JokeDTO buyJoke(long id) {
        Joke joke = jokeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, id));
        joke.setTimesBought(joke.getTimesBought() + 1);
        jokeRepository.save(joke);
        return jokeMapper.toDTO(joke);
    }

    public List<JokeDTO> getSetups() {
        List<Joke> jokes = jokeRepository.findAll();
        jokes.forEach(joke -> joke.setPunchline(null));
        return jokeMapper.toDTOList(jokes);
    }

    public List<JokeDTO> getTop3() {
        List<Joke> jokes = jokeRepository.findAllByOrderByTimesBoughtDesc();
        if (jokes.stream().allMatch(joke -> joke.getTimesBought() == 0)) {
            Collections.shuffle(jokes);
        }
        jokes = jokes.subList(0, 3);
        jokes.forEach(joke -> joke.setPunchline(null));
        return jokeMapper.toDTOList(jokes);
    }
}
