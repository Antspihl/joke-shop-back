package ee.veebiprojekt.veebiprojekttest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Random;

@RestController
public class Controller {
    Integer gameIDCounter = 1;
    HashMap<Integer, Integer> games = new HashMap<>();
    HashMap<Integer, Integer> gameGuesses = new HashMap<>();

    @GetMapping()
    public String mainMessage() {
        return "Guess the number";
    }

    @GetMapping("/game")
    public String getNumber() {
        Random random = new Random();
        int random_number = random.nextInt(100);
        int gameId = gameIDCounter;
        gameIDCounter++;
        games.put(gameId, random_number);
        gameGuesses.put(gameId, 0);
        return String.format("Your game ID is %d", gameId);
    }

    @GetMapping("/game/{game_id}/guess/{number}")
    public String guessNumber(@PathVariable("game_id") Integer gameId, @PathVariable("number") Integer number) {
        if (!games.containsKey(gameId)) return "Wrong game ID!";
        int answer = games.get(gameId);
        gameGuesses.put(gameId, gameGuesses.get(gameId) + 1);
        if (number > answer) {
            return "Number you are trying to guess is smaller";
        }
        else if (number < answer) {
            return "Number you are trying to guess is bigger";
        }
        return String.format("Correct, it took you %d times", gameGuesses.get(gameId));
    }
    write me a code
}
