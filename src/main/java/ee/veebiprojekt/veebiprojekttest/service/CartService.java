package ee.veebiprojekt.veebiprojekttest.service;

import ee.veebiprojekt.veebiprojekttest.dto.CartDTO;
import ee.veebiprojekt.veebiprojekttest.dto.CartItemDTO;
import ee.veebiprojekt.veebiprojekttest.entity.Cart;
import ee.veebiprojekt.veebiprojekttest.entity.CartItem;
import ee.veebiprojekt.veebiprojekttest.exception.EntityNotFoundException;
import ee.veebiprojekt.veebiprojekttest.mapper.CartItemMapper;
import ee.veebiprojekt.veebiprojekttest.mapper.CartMapper;
import ee.veebiprojekt.veebiprojekttest.repository.CartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    public CartService(CartRepository cartRepository, CartMapper cartMapper, CartItemMapper cartItemMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.cartItemMapper = cartItemMapper;
    }

    public CartDTO addCart(CartDTO cartDTO) {
        log.debug("Adding cart with user id {}", cartDTO.userId());
        Cart cart = new Cart();
        cart.setUserId(cartDTO.userId());
        cartRepository.save(cart);
        log.debug("Added cart with id {}", cart.getCartId());
        return cartMapper.toDTO(cart);
    }

    public CartDTO getCart(long id) {
        log.debug("Getting cart with id {}", id);
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cart", id));
        return cartMapper.toDTO(cart);
    }

    public List<CartItemDTO> getCartItems(long id) {
        log.debug("Getting cart items for cart with id {}", id);
        List<CartItem> cartItems = cartRepository.findCartItemsByCartId(id);
        return cartItems.stream()
                .map(cartItemMapper::toDTO)
                .toList();
    }

    public void removeCart(long id) {
        log.debug("Removing cart with id {}", id);
        cartRepository.deleteById(id);
    }
}
