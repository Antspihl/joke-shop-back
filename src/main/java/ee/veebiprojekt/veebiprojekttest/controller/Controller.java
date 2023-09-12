package ee.veebiprojekt.veebiprojekttest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ee.veebiprojekt.veebiprojekttest.repository.Joke;
import ee.veebiprojekt.veebiprojekttest.repository.JokeRepository;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Controller {
    private final JokeRepository jokeRepository;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/add/{setup}/{punchline}")
    public String addJoke(@PathVariable("setup") String setup, @PathVariable("punchline") String punchline) {
        Joke newJoke = new Joke();
        newJoke.setSetup(setup);
        newJoke.setPunchline(punchline);
        System.out.println("enne");
        jokeRepository.save(newJoke);
        System.out.println("enne");
        return String.format("%s %s", setup, punchline);
    }

    @GetMapping("/get")
    public List<Joke> getAllJokes() {
        return jokeRepository.findAll();
    }
}
