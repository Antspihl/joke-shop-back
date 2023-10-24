package ee.veebiprojekt.veebiprojekttest.mapper;

import ee.veebiprojekt.veebiprojekttest.dto.JokeDTO;
import ee.veebiprojekt.veebiprojekttest.entity.Joke;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JokeMapper {
    JokeDTO toDTO(Joke joke);
    Joke toEntity(JokeDTO jokeDTO);

    List<JokeDTO> toDTOList(List<Joke> joke);
}
