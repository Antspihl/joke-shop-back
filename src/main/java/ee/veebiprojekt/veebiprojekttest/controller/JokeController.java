package ee.veebiprojekt.veebiprojekttest.controller;

import ee.veebiprojekt.veebiprojekttest.dto.JokeDTO;
import ee.veebiprojekt.veebiprojekttest.service.JokeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/jokes")
@RequiredArgsConstructor
@Slf4j
public class JokeController {
    private final JokeService jokeService;

    @PostMapping("/add")
    public JokeDTO addJoke(@RequestBody JokeDTO jokeDTO, Principal principal) {
        log.debug("REST request to add joke: {}", jokeDTO);
        return jokeService.addJoke(jokeDTO, principal.getName());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get/{id}")
    public JokeDTO getJoke(@PathVariable("id") long id) {
        log.debug("REST request to get joke: {}", id);
        return jokeService.getJoke(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public List<JokeDTO> getJokes() {
        log.debug("REST request to get all jokes");
        return jokeService.getJokes();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public JokeDTO editJoke(@PathVariable("id") long id, @RequestBody JokeDTO newJoke) {
        log.debug("REST request to edit joke: {}", id);
        return jokeService.editJoke(id, newJoke);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void removeJoke(@PathVariable("id") long id) {
        log.debug("REST request to remove joke: {}", id);
        jokeService.deleteJoke(id);
    }

    @GetMapping("/buy/{id}")
    public JokeDTO buyJoke(@PathVariable("id") long id, Principal principal) {
        log.debug("REST request to buy joke: {}", id);
        return jokeService.buyJoke(id, principal.getName());
    }

    @GetMapping("/setups")
    public List<JokeDTO> getSetups(Principal principal) {
        if (principal == null) return jokeService.getSetups();
        else return jokeService.getSetups(principal.getName());
    }

    @GetMapping("/top3")
    public List<JokeDTO> getTop3(Principal principal) {
        log.debug("REST request to get top 3 jokes");
        if (principal == null) return jokeService.getTop3();
        else return jokeService.getTop3(principal.getName());
    }

    @GetMapping("/bought")
    public List<JokeDTO> getBoughtJokes(Principal principal) {
        log.debug("REST request to get bought jokes");
        return jokeService.getBoughtJokes(principal.getName());
    }
}
