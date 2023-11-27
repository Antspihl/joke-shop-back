package ee.veebiprojekt.veebiprojekttest.service;

import ee.veebiprojekt.veebiprojekttest.dto.JokeDTO;
import ee.veebiprojekt.veebiprojekttest.entity.BoughtJoke;
import ee.veebiprojekt.veebiprojekttest.entity.Joke;
import ee.veebiprojekt.veebiprojekttest.entity.Rating;
import ee.veebiprojekt.veebiprojekttest.entity.User;
import ee.veebiprojekt.veebiprojekttest.exception.EntityNotFoundException;
import ee.veebiprojekt.veebiprojekttest.mapper.JokeMapper;
import ee.veebiprojekt.veebiprojekttest.repository.BoughtJokeRepository;
import ee.veebiprojekt.veebiprojekttest.repository.JokeRepository;
import ee.veebiprojekt.veebiprojekttest.repository.RatingRepository;
import ee.veebiprojekt.veebiprojekttest.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final BoughtJokeRepository boughtJokeRepository;
    private static final String ENTITY_NAME = "joke";

    public JokeService(JokeRepository jokeRepository, JokeMapper jokeMapper, RatingRepository ratingRepository, UserRepository userRepository, BoughtJokeRepository boughtJokeRepository, JsonReader jsonReader) {
        this.jokeRepository = jokeRepository;
        this.jokeMapper = jokeMapper;
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
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

    public JokeDTO buyJoke(long jokeId, String username) {
        log.debug("Request joke {} for {}", jokeId, username);
        User user = userRepository.findByUsername(username);
        Joke joke = jokeRepository.findById(jokeId).orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, jokeId));
        joke.setTimesBought(joke.getTimesBought() + 1);
        jokeRepository.save(joke);
        boughtJokeRepository.save(new BoughtJoke(user.getUserId(), jokeId));

        log.debug("Bought Joke {} for {}", joke, username);
        return jokeMapper.toDTO(joke);
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

    public List<JokeDTO> getBoughtJokes(String username) {
        log.debug("Request to get bought Jokes");
        Long userId = userRepository.findByUsername(username).getUserId();
        List<Long> boughtJokeIds = boughtJokeRepository.findJokeIdsByUserId(userId);
        List<Joke> jokes = jokeRepository.findAllById(boughtJokeIds);
        log.debug("Bought Jokes : {}", jokes);
        return jokeMapper.toDTOList(jokes);
    }

    public List<JokeDTO> getSetups() {
        log.debug("Request to get all Joke setups");
        List<Joke> jokes = jokeRepository.findAll();
        jokes.forEach(joke -> joke.setPunchline(null));
        return jokeMapper.toDTOList(jokes);
    }

    public List<JokeDTO> getSetups(String username) {
        log.debug("Request to get all Joke setups for {}", username);
        Long userId = userRepository.findByUsername(username).getUserId();
        List<Long> boughtJokeIds = boughtJokeRepository.findJokeIdsByUserId(userId);
        List<Joke> jokes = jokeRepository.findAll();
        jokes.removeIf(joke -> boughtJokeIds.contains(joke.getId()));
        jokes.forEach(joke -> joke.setPunchline(null));
        return jokeMapper.toDTOList(jokes);
    }

    public JSONObject getRandomJoke() throws IOException {
        log.debug("Request to get random joke");
//        URL url = new URL("https://v2.jokeapi.dev/joke/Any");
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("GET");
//        connection.setConnectTimeout(1500);
//        connection.setReadTimeout(1500);
        JSONObject response = JsonReader.readJsonFromUrl("https://v2.jokeapi.dev/joke/Any");
        return response;
    }
}
