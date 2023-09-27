package ee.veebiprojekt.veebiprojekttest.controller;

import ee.veebiprojekt.veebiprojekttest.dto.CartDTO;
import ee.veebiprojekt.veebiprojekttest.dto.CartItemDTO;
import ee.veebiprojekt.veebiprojekttest.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    public CartDTO addCart(@RequestBody CartDTO cartDTO) {
        return cartService.addCart(cartDTO);
    }

    @GetMapping("/get/{id}")
    public CartDTO getCart(@PathVariable long id) {
        return cartService.getCart(id);
    }

    @GetMapping("/get_items/{id}")
    public List<CartItemDTO> getCartItems(@PathVariable long id) {
        return cartService.getCartItems(id);
    }

    @DeleteMapping("{id}")
    public void removeCart(@PathVariable long id) {
        cartService.removeCart(id);
    }
}
