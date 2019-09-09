package com.codecool.controller;

import com.codecool.model.*;
import com.codecool.repository.CartItemsRepository;
import com.codecool.repository.CartRepository;
import com.codecool.repository.ProductsRepository;
import com.codecool.repository.UserRepository;
import com.codecool.security.JwtTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class CartController {

    @Autowired
    private JwtTokenServices jwtTokenServices;

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
        String token = orderDto.getToken();
        token = token.replaceAll("\"", "");
        int productId = orderDto.getProductId();
        int quantity = orderDto.getQuantity();
        Products products = productsRepository.findById(productId);

        if (jwtTokenServices.validateToken(token)) {
            Authentication auth = jwtTokenServices.parseUserFromTokenInfo(token);
            String name = auth.getName();
            Users user = userRepository.findByName(name)
                    .orElseThrow(() -> new UsernameNotFoundException("Username: " + name + " not found"));

            List<Carts> cartsList = em.createNamedQuery("findAvailableCartsByUserId", Carts.class)
                    .setParameter("user", user).getResultList();

            if (cartsList.size() == 0) {
                Carts newCart = cartRepository.save(new Carts(user, 0, false));
                CartItems save = cartItemsRepository.save(new CartItems(newCart, products, quantity, quantity * products.getPrice()));
            } else {
                CartItems save = cartItemsRepository.save(new CartItems(cartsList.get(0), products, quantity, quantity * products.getPrice()));
            }
        }
    }

    @GetMapping(path = "/myCart")
    public List<CartDto> getMyCart(@RequestParam String token) {
        token = token.replaceAll("\"", "");
        if (jwtTokenServices.validateToken(token)) {
            Authentication auth = jwtTokenServices.parseUserFromTokenInfo(token);
            String name = auth.getName();
            Users user = userRepository.findByName(name)
                    .orElseThrow(() -> new UsernameNotFoundException("Username: " + name + " not found"));

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
        return null;
    }

    @PutMapping(path = "/checkout")
    public @ResponseBody
    void checkoutCart(@RequestBody JWTToken data) {
        String token = data.getToken();
        token = token.replaceAll("\"", "");
        if (jwtTokenServices.validateToken(token)) {
            Authentication auth = jwtTokenServices.parseUserFromTokenInfo(token);
            String name = auth.getName();
            Users user = userRepository.findByName(name)
                    .orElseThrow(() -> new UsernameNotFoundException("Username: " + name + " not found"));

            List<Carts> cartsList = em.createNamedQuery("findAvailableCartsByUserId", Carts.class)
                    .setParameter("user", user).getResultList();

            cartsList.get(0).setCheckedOut(true);
            cartRepository.save(cartsList.get(0));

        }
    }

    @DeleteMapping(path = "/deleteItem")
    public @ResponseBody
    void deleteFromCart(@RequestParam int productId, String token){
        token = token.replaceAll("\"", "");
        if (jwtTokenServices.validateToken(token)) {
            Authentication auth = jwtTokenServices.parseUserFromTokenInfo(token);
            String name = auth.getName();
            Users user = userRepository.findByName(name)
                    .orElseThrow(() -> new UsernameNotFoundException("Username: " + name + " not found"));

            Products byId = productsRepository.findById(productId);
            cartItemsRepository.deleteByProductAndCart_User(byId, user);
        }

    }

}
