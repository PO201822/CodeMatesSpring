package com.codecool.controller;

import com.codecool.dto.CartItemDto;
import com.codecool.dto.CartOrderDto;
import com.codecool.dto.OrderDetailsDto;
import com.codecool.dto.OrderDto;
import com.codecool.entity.CartItems;
import com.codecool.entity.Carts;
import com.codecool.entity.Orders;
import com.codecool.model.CartItemDetail;
import com.codecool.service.CartItemsService;
import com.codecool.service.CartService;
import com.codecool.service.OrderService;
import com.codecool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class CartController {

    @Autowired
    private UserService simpleUserService;

    @Autowired
    private CartService simpleCartService;

    @Autowired
    private CartItemsService simpleCartItemsService;

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
        List<CartOrderDto> cartOrderDtos = new ArrayList<>();
        List<Carts> carts = simpleCartService.getAvailableCarts(simpleUserService.getCurrentUser());
        if (carts.size() == 0) {
            return null;
        }

        for (Carts cart : carts) {
            OrderDetailsDto orderDetails = new OrderDetailsDto(false, null, new CartItemDetail(0, 0), "Waiting for pick up");
            CartItemDetail cartItemDetail = simpleCartItemsService.calculateCartItemsDetail(cart);
            if (cart.isPickedup()) {
                Orders order = simpleOrderService.findByCartId(cart.getId());
                simpleOrderService.updateOrderDetailsDtoByOrder(orderDetails, order);
            }
            cartOrderDtos.add(new CartOrderDto(cart, cart.getCartItems(), cartItemDetail, cart.getUser(), orderDetails.getStatus()));
        }
        return cartOrderDtos;
    }
}
