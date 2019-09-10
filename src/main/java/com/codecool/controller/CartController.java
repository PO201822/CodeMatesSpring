package com.codecool.controller;

import com.codecool.model.*;
import com.codecool.repository.CartItemsRepository;
import com.codecool.repository.CartRepository;
import com.codecool.repository.ProductsRepository;
import com.codecool.repository.UserRepository;
import com.codecool.security.JwtTokenServices;
import com.codecool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;


@RestController
public class CartController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @PersistenceContext
    private EntityManager em;

    @PostMapping(path = "/addToCart")
    public void addToCart(@RequestBody OrderDto orderDto) {
        int productId = orderDto.getProductId();
        int quantity = orderDto.getQuantity();
        Products products = productsRepository.findById(productId);
        Users user = userRepository.findByName(userService.currentUser());

        List<Carts> cartsList = em.createNamedQuery("findAvailableCartsByUserId", Carts.class)
                .setParameter("user", user).getResultList();


        if (cartsList.size() == 0) {
            Carts newCart = cartRepository.save(new Carts(user, 0, false));
            CartItems save = cartItemsRepository.save(new CartItems(newCart, products, quantity, quantity * products.getPrice()));
        } else {
            CartItems save = cartItemsRepository.save(new CartItems(cartsList.get(0), products, quantity, quantity * products.getPrice()));
        }

    }

    @GetMapping(path = "/myCart")
    public List<CartDto> getMyCart() {
        Users user = userRepository.findByName(userService.currentUser());
        List<Carts> cartsList = em.createNamedQuery("findAvailableCartsByUserId", Carts.class)
                .setParameter("user", user).getResultList();

        if (cartsList.size() == 0) {
            //the cart is empty
            return null;
        } else {
            List<CartItems> allFromCart = em.createNamedQuery("findCartItemsByCartId", CartItems.class)
                    .setParameter("cart_id", cartsList.get(0).getId()).getResultList();

            List<CartDto> cartDtos = new ArrayList<>();
            for (CartItems cartItems : allFromCart) {
                Products product = cartItems.getProduct();
                cartDtos.add(new CartDto(product, cartItems.getQuantity(), cartsList.get(0)));
            }
            return cartDtos;
        }
    }

    @PutMapping(path = "/checkout")
    public @ResponseBody
    void checkoutCart() {
        Users user = userRepository.findByName(userService.currentUser());
        List<Carts> cartsList = em.createNamedQuery("findAvailableCartsByUserId", Carts.class)
                .setParameter("user", user).getResultList();

        cartsList.get(0).setCheckedOut(true);
        cartRepository.save(cartsList.get(0));
    }

    @DeleteMapping(path = "/deleteItem")
    @Transactional
    public @ResponseBody
    void deleteFromCart(@RequestParam int productId) {
        Products byId = productsRepository.findById(productId);
        Users user = userRepository.findByName(userService.currentUser());
        List<Carts> cartsList = em.createNamedQuery("findAvailableCartsByUserId", Carts.class)
                .setParameter("user", user).getResultList();

        em.createNamedQuery("deleteProductFromCart").setParameter("product", byId).setParameter("cart", cartsList.get(0)).executeUpdate();
    }
}
