package ee.veebiprojekt.veebiprojekttest.service;

import ee.veebiprojekt.veebiprojekttest.dto.CartItemDTO;
import ee.veebiprojekt.veebiprojekttest.entity.CartItem;
import ee.veebiprojekt.veebiprojekttest.exception.EntityNotFoundException;
import ee.veebiprojekt.veebiprojekttest.mapper.CartItemMapper;
import ee.veebiprojekt.veebiprojekttest.repository.CartItemRepository;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;


    public CartItemService(CartItemRepository cartItemRepository, CartItemMapper cartItemMapper) {
        this.cartItemRepository = cartItemRepository;
        this.cartItemMapper = cartItemMapper;
    }

    public CartItemDTO addCartItem(CartItemDTO cartItemDTO) {
        CartItem cartItem = new CartItem();
        cartItem.setCartId(cartItemDTO.cartId());
        cartItem.setJokeId(cartItemDTO.jokeId());
        cartItemRepository.save(cartItem);
        return cartItemMapper.toDTO(cartItem);
    }

    public CartItemDTO getCartItem(long id) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("CartItem", id));
        return cartItemMapper.toDTO(cartItem);
    }

    public void removeCartItem(long id) {
        cartItemRepository.deleteById(id);
    }
}
