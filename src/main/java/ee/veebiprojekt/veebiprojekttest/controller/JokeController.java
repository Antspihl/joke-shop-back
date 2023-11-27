package ee.veebiprojekt.veebiprojekttest.controller;

import ee.veebiprojekt.veebiprojekttest.dto.JokeDTO;
import ee.veebiprojekt.veebiprojekttest.service.JokeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jokes")
@RequiredArgsConstructor
@Slf4j
public class JokeController {
    private final JokeService jokeService;

    @PostMapping("/add")
    public JokeDTO addJoke(@RequestBody JokeDTO jokeDTO) {
        log.debug("REST request to add joke: {}", jokeDTO);
        return jokeService.addJoke(jokeDTO);
    }

    @GetMapping("/get/{id}")
    public JokeDTO getJoke(@PathVariable("id") long id) {
        log.debug("REST request to get joke: {}", id);
        return jokeService.getJoke(id);
    }

    @GetMapping("/all")
    public List<JokeDTO> getJokes() {
        log.debug("REST request to get all jokes");
        return jokeService.getJokes();
    }

    @PutMapping("/{id}")
    public JokeDTO editJoke(@PathVariable("id") long id, @RequestBody JokeDTO newJoke) {
        log.debug("REST request to edit joke: {}", id);
        return jokeService.editJoke(id, newJoke);
    }

    @DeleteMapping("/{id}")
    public void removeJoke(@PathVariable("id") long id) {
        log.debug("REST request to remove joke: {}", id);
        jokeService.deleteJoke(id);
    }

    @GetMapping("/buy/{id}")
    public JokeDTO buyJoke(@PathVariable("id") long id) {
        log.debug("REST request to buy joke: {}", id);
        return jokeService.buyJoke(id);
    }

    @GetMapping("/setups")
    public List<JokeDTO> getSetups() {
        log.debug("REST request to get setups");
        return jokeService.getSetups();
    }

    @GetMapping("/top3")
    public List<JokeDTO> getTop3() {
        log.debug("REST request to get top 3 jokes");
        return jokeService.getTop3();
    }

}
