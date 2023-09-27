package ee.veebiprojekt.veebiprojekttest.mapper;

import ee.veebiprojekt.veebiprojekttest.dto.CartItemDTO;
import ee.veebiprojekt.veebiprojekttest.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartItemMapper {
    CartItemDTO toDTO(CartItem cartItem);
    CartItem toEntity(CartItemDTO cartItemDTO);
}
