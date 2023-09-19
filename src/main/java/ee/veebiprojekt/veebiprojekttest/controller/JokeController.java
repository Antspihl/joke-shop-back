package ee.veebiprojekt.veebiprojekttest.controller;

import ee.veebiprojekt.veebiprojekttest.dto.JokeDTO;
import ee.veebiprojekt.veebiprojekttest.service.JokeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ee.veebiprojekt.veebiprojekttest.entity.Joke;

import java.util.List;

@RestController
@RequestMapping("/api/jokes")
@RequiredArgsConstructor
public class JokeController {
    private final JokeService jokeService;

    @PostMapping
    public JokeDTO addJoke(@RequestBody JokeDTO jokeDTO) {
        return jokeService.addJoke(jokeDTO);
    }

    @GetMapping("/{id}")
    public JokeDTO getJoke(@PathVariable long id) {
        return jokeService.getJoke(id);
    }

    @GetMapping()
    public List<Joke> getJokes() {
        return jokeService.getJokes();
    }

}
