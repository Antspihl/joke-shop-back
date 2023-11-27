package ee.veebiprojekt.veebiprojekttest.controller;

import ee.veebiprojekt.veebiprojekttest.dto.CartDTO;
import ee.veebiprojekt.veebiprojekttest.dto.CartItemDTO;
import ee.veebiprojekt.veebiprojekttest.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final CartService cartService;

    @PostMapping
    public CartDTO addCart(@RequestBody CartDTO cartDTO) {
        log.debug("REST request to add cart: {}", cartDTO);
        return cartService.addCart(cartDTO);
    }

    @GetMapping("/get/{id}")
    public CartDTO getCart(@PathVariable long id) {
        log.debug("REST request to get cart: {}", id);
        return cartService.getCart(id);
    }

    @GetMapping("/get_items/{id}")
    public List<CartItemDTO> getCartItems(@PathVariable long id) {
        log.debug("REST request to get cart items: {}", id);
        return cartService.getCartItems(id);
    }

    @DeleteMapping("/{id}")
    public void removeCart(@PathVariable long id) {
        log.debug("REST request to remove cart: {}", id);
        cartService.removeCart(id);
    }
}
