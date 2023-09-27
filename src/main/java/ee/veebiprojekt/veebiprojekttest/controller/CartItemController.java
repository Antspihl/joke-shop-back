package ee.veebiprojekt.veebiprojekttest.controller;

import ee.veebiprojekt.veebiprojekttest.dto.CartItemDTO;
import ee.veebiprojekt.veebiprojekttest.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/cart_items")
@RequiredArgsConstructor
public class CartItemController {
    private final CartItemService cartItemService;

    @PostMapping
    public CartItemDTO addCartItem(@RequestBody CartItemDTO cartItemDTO) {
        return cartItemService.addCartItem(cartItemDTO);
    }

    @GetMapping("{id}")
    public CartItemDTO getCartItem(@PathVariable long id) {
        return cartItemService.getCartItem(id);
    }

    @DeleteMapping("{id}")
    public void removeCartItem(@PathVariable long id) {
        cartItemService.removeCartItem(id);
    }
}
