package ee.veebiprojekt.veebiprojekttest.mapper;

import ee.veebiprojekt.veebiprojekttest.dto.UserDTO;
import ee.veebiprojekt.veebiprojekttest.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);
}
