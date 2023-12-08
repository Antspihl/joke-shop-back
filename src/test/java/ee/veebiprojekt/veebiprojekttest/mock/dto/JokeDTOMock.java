package ee.veebiprojekt.veebiprojekttest.mock.dto;

import ee.veebiprojekt.veebiprojekttest.dto.JokeDTO;

import java.math.BigDecimal;

public class JokeDTOMock {
    public static JokeDTO shallowJokeDTO(long l) {
        return new JokeDTO(l, "Mock Setup", "Mock Punchline", new BigDecimal(1), 0L, 0, 0);
    }

    public static JokeDTO shallowEditedJokeDTO(long l) {
        return new JokeDTO(l, "Mock Setup edited", "Mock Punchline edited", new BigDecimal(2), 1L, 1, 1);
    }

    public static JokeDTO shallowSetupJokeDTO(long l) {
        return new JokeDTO(l, "Mock Setup", null, new BigDecimal(1), 1L, 5, 0);
    }
}
