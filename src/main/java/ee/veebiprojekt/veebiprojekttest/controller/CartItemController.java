package ee.veebiprojekt.veebiprojekttest.controller;

import ee.veebiprojekt.veebiprojekttest.dto.CartItemDTO;
import ee.veebiprojekt.veebiprojekttest.service.CartItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/cart_items")
@RequiredArgsConstructor
@Slf4j
public class CartItemController {
    private final CartItemService cartItemService;

    @PostMapping
    public CartItemDTO addCartItem(@RequestBody CartItemDTO cartItemDTO) {
        log.debug("REST request to add cart item: {}", cartItemDTO);
        return cartItemService.addCartItem(cartItemDTO);
    }

    @GetMapping("{id}")
    public CartItemDTO getCartItem(@PathVariable long id) {
        log.debug("REST request to get cart item: {}", id);
        return cartItemService.getCartItem(id);
    }

    @DeleteMapping("{id}")
    public void removeCartItem(@PathVariable long id) {
        log.debug("REST request to remove cart item: {}", id);
        cartItemService.removeCartItem(id);
    }
}
