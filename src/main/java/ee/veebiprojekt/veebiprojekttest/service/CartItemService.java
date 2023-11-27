package ee.veebiprojekt.veebiprojekttest.service;

import ee.veebiprojekt.veebiprojekttest.dto.CartItemDTO;
import ee.veebiprojekt.veebiprojekttest.entity.CartItem;
import ee.veebiprojekt.veebiprojekttest.exception.EntityNotFoundException;
import ee.veebiprojekt.veebiprojekttest.mapper.CartItemMapper;
import ee.veebiprojekt.veebiprojekttest.repository.CartItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;


    public CartItemService(CartItemRepository cartItemRepository, CartItemMapper cartItemMapper) {
        this.cartItemRepository = cartItemRepository;
        this.cartItemMapper = cartItemMapper;
    }

    public CartItemDTO addCartItem(CartItemDTO cartItemDTO) {
        log.debug("Adding cart item with cart id {} and joke id {}", cartItemDTO.cartId(), cartItemDTO.jokeId());
        CartItem cartItem = new CartItem();
        cartItem.setCartId(cartItemDTO.cartId());
        cartItem.setJokeId(cartItemDTO.jokeId());
        cartItemRepository.save(cartItem);
        log.debug("Added cart item with id {}", cartItem.getId());
        return cartItemMapper.toDTO(cartItem);
    }

    public CartItemDTO getCartItem(long id) {
        log.debug("Getting cart item with id {}", id);
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("CartItem", id));
        return cartItemMapper.toDTO(cartItem);
    }

    public void removeCartItem(long id) {
        log.debug("Removing cart item with id {}", id);
        cartItemRepository.deleteById(id);
    }
}
