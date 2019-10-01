package com.codecool.service.simple;

import com.codecool.dto.CartItemDto;
import com.codecool.dto.OrderDto;
import com.codecool.entity.CartItems;
import com.codecool.entity.Carts;
import com.codecool.entity.Products;
import com.codecool.entity.Users;
import com.codecool.repository.*;
import com.codecool.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SimpleCartService implements CartService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @PersistenceContext
    private EntityManager em;

    public void addToCart(OrderDto orderedProduct, Users user) {
        int quantity = orderedProduct.getQuantity();
        Products product = productsRepository.findById(orderedProduct.getProductId());

        Optional<Carts> cart = cartRepository.findByUserIdAndCheckedOut(user.getId(), false);

        if (!cart.isPresent()) {
            Carts newCart = cartRepository.save(new Carts(user, 0, false));
            CartItems save = cartItemsRepository.save(new CartItems(newCart, product, quantity, product.getPrice()));
        } else {
            boolean isItemInCart = false;

            List<CartItems> cartItems = cart.get().getCartItems();

            for (CartItems cartItem : cartItems) {
                if (cartItem.getProduct().equals(product)) {
                    cartItem.setQuantity(quantity += cartItem.getQuantity());
                    cartItemsRepository.save(cartItem);
                    isItemInCart = true;
                }
            }
            if (!isItemInCart) {
                CartItems save = cartItemsRepository.save(new CartItems(cart.get(), product, quantity, product.getPrice()));
            }
        }
    }

    public List<CartItems> getMyCart(Users user) {
        if (cartRepository.findAllByUserId(user.getId()).size() == 0) {
            //no cart yet
            return null;
        }

        List<Carts> cartsList = cartRepository.findAllByUserIdAndCheckedOut(user.getId(), false);

        if (cartsList.size() == 0 || cartsList.get(0).getCartItems().size() == 0) {
            //none available
            return null;
        } else {
            return cartItemsRepository.findAllByCartId(cartsList.get(0).getId());
            /*em.createNamedQuery("findCartItemsByCartId", CartItems.class)
                    .setParameter("cart_id", cartsList.get(0).getId()).getResultList();*/
        }

    }

    public void checkoutCart(Users user) {
        List<Carts> cartsList = cartRepository.findAllByUserIdAndCheckedOut(user.getId(), false);

        cartsList.get(0).setCheckedOut(true);
        cartsList.get(0).setCheckout_date(LocalDateTime.now());
        cartRepository.save(cartsList.get(0));
    }

    public void updateCartItems(CartItemDto cartItemDto, Users user) {
        CartItems cartItems = cartItemsRepository.findById(cartItemDto.getCartItemId());
        cartItems.setQuantity(cartItemDto.getQuantity());
        cartItemsRepository.save(cartItems);
    }

    public void deleteFromCart(int productId, Users user) {
        Products byId = productsRepository.findById(productId);
        List<Carts> cartsList = cartRepository.findAllByUserIdAndCheckedOut(user.getId(), false);

        em.createNamedQuery("deleteProductFromCart").setParameter("product", byId).setParameter("cart", cartsList.get(0)).executeUpdate();
    }

    public List<Carts> getAvailableCarts(Users user) {
        return cartRepository.findAllByUserIdAndCheckedOut(user.getId(), true);
    }

}