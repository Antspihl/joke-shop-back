package ee.veebiprojekt.veebiprojekttest.service;

import ee.veebiprojekt.veebiprojekttest.dto.CartDTO;
import ee.veebiprojekt.veebiprojekttest.dto.CartItemDTO;
import ee.veebiprojekt.veebiprojekttest.entity.Cart;
import ee.veebiprojekt.veebiprojekttest.entity.CartItem;
import ee.veebiprojekt.veebiprojekttest.exception.EntityNotFoundException;
import ee.veebiprojekt.veebiprojekttest.mapper.CartItemMapper;
import ee.veebiprojekt.veebiprojekttest.mapper.CartMapper;
import ee.veebiprojekt.veebiprojekttest.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
        Cart cart = new Cart();
        cart.setUserId(cartDTO.userId());
        cartRepository.save(cart);
        return cartMapper.toDTO(cart);
    }

    public CartDTO getCart(long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cart", id));
        return cartMapper.toDTO(cart);
    }

    public List<CartItemDTO> getCartItems(long id) {
        List<CartItem> cartItems = cartRepository.findCartItemsByCartId(id);
        return cartItems.stream()
                .map(cartItemMapper::toDTO)
                .toList();
    }

    public void removeCart(long id) {
        cartRepository.deleteById(id);
    }
}
