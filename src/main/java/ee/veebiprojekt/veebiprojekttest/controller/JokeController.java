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

    @PostMapping("/add")
    public JokeDTO addJoke(@RequestBody JokeDTO jokeDTO) {
        return jokeService.addJoke(jokeDTO);
    }

    @GetMapping("/get/{id}")
    public JokeDTO getJoke(@PathVariable("id") long id) {
        return jokeService.getJoke(id);
    }

    @GetMapping("/all")
    public List<Joke> getJokes() {
        return jokeService.getJokes();
    }

    @PutMapping("/{id}")
    public JokeDTO editJoke(@PathVariable("id") long id, @RequestBody JokeDTO newJoke) {
        return jokeService.editJoke(id, newJoke);
    }

    @DeleteMapping("/{id}")
    public void removeJoke(@PathVariable("id") long id) {
        jokeService.deleteJoke(id);
    }

    @GetMapping("/setups")
    public List<JokeDTO> getSetups() {
        return jokeService.getSetups();
    }

    @GetMapping("buy/{id}")
    public JokeDTO buyJoke(@PathVariable("id") long id) {
        return jokeService.buyJoke(id);
    }
}
