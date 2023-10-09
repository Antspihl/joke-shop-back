package ee.veebiprojekt.veebiprojekttest.dto;

import java.math.BigDecimal;

public record JokeDTO(Long id, String setup, String punchline, BigDecimal price, Long timesBought) {
}
