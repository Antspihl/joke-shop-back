package ee.veebiprojekt.veebiprojekttest.service;

import ee.veebiprojekt.veebiprojekttest.dto.JokeDTO;
import ee.veebiprojekt.veebiprojekttest.dto.RatingDTO;
import ee.veebiprojekt.veebiprojekttest.entity.Joke;
import ee.veebiprojekt.veebiprojekttest.entity.Rating;
import ee.veebiprojekt.veebiprojekttest.mapper.JokeMapper;
import ee.veebiprojekt.veebiprojekttest.repository.JokeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JokeService {
    private final JokeRepository jokeRepository;
    private final JokeMapper jokeMapper;

    public JokeService(JokeRepository jokeRepository, JokeMapper jokeMapper) {
        this.jokeRepository = jokeRepository;
        this.jokeMapper = jokeMapper;
    }

    public JokeDTO addJoke(JokeDTO jokeDTO) {
        Joke joke = new Joke();
        joke.setSetup(jokeDTO.setup());
        joke.setPunchline(jokeDTO.punchline());
        jokeRepository.save(joke);
        return jokeMapper.toDTO(joke);
    }
}
