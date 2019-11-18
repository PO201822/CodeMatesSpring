package com.codecool.controller;

import com.codecool.dto.CartItemDto;
import com.codecool.dto.CartOrderDto;
import com.codecool.dto.OrderDto;
import com.codecool.entity.CartItems;
import com.codecool.service.CartService;
import com.codecool.service.OrderService;
import com.codecool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private UserService simpleUserService;

    @Autowired
    private CartService simpleCartService;

    @Autowired
    private OrderService simpleOrderService;


    @PostMapping(path = "/addToCart")
    public void addToCart(@RequestBody OrderDto orderedProduct) {
        simpleCartService.addToCart(orderedProduct, simpleUserService.getCurrentUser());
    }

    @GetMapping(path = "/myCart")
    public List<CartItems> getMyCart() {
        return simpleCartService.getMyCart(simpleUserService.getCurrentUser());
    }

    @PutMapping(path = "/checkout")
    public @ResponseBody
    void checkoutCart() {
        simpleCartService.checkoutCart(simpleUserService.getCurrentUser());
    }

    @PutMapping(path = "/updateCartItemQuantity")
    public @ResponseBody
    void updateCartItems(@RequestBody CartItemDto cartItemDto) {
        simpleCartService.updateCartItems(cartItemDto, simpleUserService.getCurrentUser());
    }

    @DeleteMapping(path = "/deleteItem")
    @Transactional
    public @ResponseBody
    void deleteFromCart(@RequestParam int productId) {
        simpleCartService.deleteFromCart(productId, simpleUserService.getCurrentUser());
    }

    @GetMapping(path = "/getOrders")
    public List<CartOrderDto> getOrders() {
        return simpleOrderService.getOrders();
    }
}
