package ee.veebiprojekt.veebiprojekttest.dto;

import java.math.BigDecimal;

public record JokeDTO(long id, String setup, String punchline, BigDecimal price, Long timesBought, double rating, long createdBy) {
}
