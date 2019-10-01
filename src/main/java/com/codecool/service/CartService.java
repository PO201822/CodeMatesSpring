package com.codecool.service;

import com.codecool.dto.CartItemDto;
import com.codecool.dto.OrderDto;
import com.codecool.entity.CartItems;
import com.codecool.entity.Carts;
import com.codecool.entity.Users;

import java.util.List;

public interface CartService {

    void addToCart(OrderDto orderedProduct, Users user);

    List<CartItems> getMyCart(Users user);

    void checkoutCart(Users user);

    void updateCartItems(CartItemDto cartItemDto, Users user);

    void deleteFromCart(int productId, Users user);

    List<Carts> getAvailableCarts(Users user);
}
