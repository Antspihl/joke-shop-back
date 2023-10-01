package ee.veebiprojekt.veebiprojekttest.mapper;

import ee.veebiprojekt.veebiprojekttest.dto.CartDTO;
import ee.veebiprojekt.veebiprojekttest.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartMapper {
    CartDTO toDTO(Cart cart);
    Cart toEntity(CartDTO cartDTO);
}
