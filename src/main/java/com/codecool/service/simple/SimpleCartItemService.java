package com.codecool.service.simple;

import com.codecool.entity.CartItems;
import com.codecool.entity.Carts;
import com.codecool.model.CartItemDetail;
import com.codecool.service.CartItemsService;
import org.springframework.stereotype.Service;

@Service
public class SimpleCartItemService implements CartItemsService {

    public CartItemDetail calculateCartItemsDetail(Carts cart) {
        int quantity = 0, total = 0;
        for (CartItems cartItem : cart.getCartItems()) {
            quantity += cartItem.getQuantity();
            total += cartItem.getQuantity() * cartItem.getPrice();
        }
        return new CartItemDetail(total, quantity);

    }
}
