package ee.veebiprojekt.veebiprojekttest.service;

import ee.veebiprojekt.veebiprojekttest.dto.JokeDTO;
import ee.veebiprojekt.veebiprojekttest.entity.Joke;
import ee.veebiprojekt.veebiprojekttest.mapper.JokeMapper;
import ee.veebiprojekt.veebiprojekttest.repository.JokeRepository;
import ee.veebiprojekt.veebiprojekttest.exception.EntityNotFoundException;
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
        if (jokeDTO.price() != null) joke.setPrice(jokeDTO.price());
        if (jokeDTO.timesBought() != null) joke.setTimesBought(jokeDTO.timesBought());
        jokeRepository.save(joke);
        return jokeMapper.toDTO(joke);
    }

    public JokeDTO getJoke(long id) {
        Joke joke = jokeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        return jokeMapper.toDTO(joke);
    }

    public List<Joke> getJokes() {
        return jokeRepository.findAll();
    }

    public JokeDTO editJoke(long id, JokeDTO newJoke) {
        Joke joke = jokeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        if (newJoke.setup() != null) joke.setSetup(newJoke.setup());
        if (newJoke.punchline() != null) joke.setPunchline(newJoke.punchline());
        if (newJoke.price() != null) joke.setPrice(newJoke.price());
        if (newJoke.timesBought() != null) joke.setTimesBought(newJoke.timesBought());
        jokeRepository.save(joke);
        return jokeMapper.toDTO(joke);
    }

    public void deleteJoke(long id) {
        jokeRepository.deleteById(id);
    }

    public List<JokeDTO> getSetups() {
        List<Joke> jokes = jokeRepository.findAll();
        jokes.forEach(joke -> joke.setPunchline(null));
        return jokeMapper.toDTOList(jokes);
    }

    public JokeDTO buyJoke(long id) {
        Joke joke = jokeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        joke.setTimesBought(joke.getTimesBought() + 1);
        jokeRepository.save(joke);
        return jokeMapper.toDTO(joke);
    }
}
