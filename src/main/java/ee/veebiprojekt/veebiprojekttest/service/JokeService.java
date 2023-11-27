package ee.veebiprojekt.veebiprojekttest.service;

import ee.veebiprojekt.veebiprojekttest.dto.JokeDTO;
import ee.veebiprojekt.veebiprojekttest.entity.Joke;
import ee.veebiprojekt.veebiprojekttest.entity.Rating;
import ee.veebiprojekt.veebiprojekttest.mapper.JokeMapper;
import ee.veebiprojekt.veebiprojekttest.repository.BoughtJokeRepository;
import ee.veebiprojekt.veebiprojekttest.repository.JokeRepository;
import ee.veebiprojekt.veebiprojekttest.exception.EntityNotFoundException;
import ee.veebiprojekt.veebiprojekttest.repository.RatingRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class JokeService {
    private final JokeRepository jokeRepository;
    private final JokeMapper jokeMapper;
    private final RatingRepository ratingRepository;
    private final BoughtJokeRepository boughtJokeRepository;
    private static final String ENTITY_NAME = "joke";

    public JokeService(JokeRepository jokeRepository, JokeMapper jokeMapper, RatingRepository ratingRepository, BoughtJokeRepository boughtJokeRepository) {
        this.jokeRepository = jokeRepository;
        this.jokeMapper = jokeMapper;
        this.ratingRepository = ratingRepository;
        this.boughtJokeRepository = boughtJokeRepository;
    }

    public JokeDTO addJoke(JokeDTO jokeDTO) {
        log.debug("Request to save Joke : {}", jokeDTO);
        Joke joke = new Joke();
        joke.setSetup(jokeDTO.setup());
        joke.setPunchline(jokeDTO.punchline());
        if (jokeDTO.price() != null) joke.setPrice(jokeDTO.price());
        if (jokeDTO.timesBought() != null) joke.setTimesBought(jokeDTO.timesBought());
        jokeRepository.save(joke);
        log.debug("Saved Joke : {}", joke);
        return jokeMapper.toDTO(joke);
    }

    public JokeDTO getJoke(long id) {
        log.debug("Request to get Joke : {}", id);
        Joke joke = jokeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, id));
        return jokeMapper.toDTO(joke);
    }

    public List<JokeDTO> getJokes() {
        log.debug("Request to get all Jokes");
        return jokeMapper.toDTOList(jokeRepository.findAll());
    }

    public JokeDTO editJoke(long id, JokeDTO newJoke) {
        log.debug("Request to edit Joke : {}", id);
        Joke joke = jokeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, id));
        if (newJoke.setup() != null) joke.setSetup(newJoke.setup());
        if (newJoke.punchline() != null) joke.setPunchline(newJoke.punchline());
        if (newJoke.price() != null) joke.setPrice(newJoke.price());
        if (newJoke.timesBought() != null) joke.setTimesBought(newJoke.timesBought());
        jokeRepository.save(joke);
        log.debug("Edited Joke : {}", joke);
        return jokeMapper.toDTO(joke);
    }

    public void recalculateJokeRating(long id) {
        log.debug("Request to recalculate Joke rating : {}", id);
        Joke joke = jokeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, id));
        List<Rating> ratings = ratingRepository.findAllByJokeId(id);
        double newRating = ratings.stream()
                .mapToInt(Rating::getRatingValue)
                .average()
                .orElse(0.0);
        joke.setRating(newRating);
        jokeRepository.save(joke);
        log.debug("Recalculated Joke rating : {}", joke);
    }

    public void deleteJoke(long id) {
        log.debug("Request to delete Joke : {}", id);
        jokeRepository.deleteById(id);
    }

    public JokeDTO buyJoke(long id) {
        log.debug("Request to buy Joke : {}", id);
        Joke joke = jokeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, id));
        joke.setTimesBought(joke.getTimesBought() + 1);
        jokeRepository.save(joke);
        log.debug("Bought Joke : {}", joke);
        return jokeMapper.toDTO(joke);
    }

    public List<JokeDTO> getSetups() {
        log.debug("Request to get all Joke setups");
        List<Joke> jokes = jokeRepository.findAll();
        jokes.forEach(joke -> joke.setPunchline(null));
        return jokeMapper.toDTOList(jokes);
    }

    public List<JokeDTO> getTop3() {
        log.debug("Request to get top 3 Jokes");
        List<Joke> jokes = jokeRepository.findAllByOrderByTimesBoughtDesc();
        if (jokes.stream().allMatch(joke -> joke.getTimesBought() == 0)) {
            Collections.shuffle(jokes);
        }
        jokes = jokes.subList(0, 3);
        jokes.forEach(joke -> joke.setPunchline(null));
        log.debug("Top 3 Jokes : {}", jokes);
        return jokeMapper.toDTOList(jokes);
    }

    public List<JokeDTO> getBoughtJokes() {
        log.debug("Request to get bought Jokes");
        List<Joke> jokes = jokeRepository.findAll();  // todo -> we need user ID here
        return jokeMapper.toDTOList(jokes);
    }

    public String getRandomJoke() throws IOException {
        log.debug("Request to get random joke");
        JSONObject response = JSONReader.readJsonFromUrl("https://v2.jokeapi.dev/joke/Programming,Dark,Spooky?type=twopart");
        return response.get("setup") + " " + response.get("delivery");
    }

}
