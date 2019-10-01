package com.codecool.service;

import com.codecool.entity.Carts;
import com.codecool.model.CartItemDetail;

public interface CartItemsService {

    CartItemDetail calculateCartItemsDetail(Carts cart);
}
