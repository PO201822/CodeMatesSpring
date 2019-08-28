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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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

    @PostMapping(path="/addToCart")
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

            if(cartsList.size() == 0){
                Carts newCart = cartRepository.save(new Carts(user, 0, false));

                CartItems save = cartItemsRepository.save(new CartItems(newCart, products, quantity, quantity*products.getPrice()));

            } else {
                CartItems save = cartItemsRepository.save(new CartItems(cartsList.get(0), products, quantity, quantity*products.getPrice()));

            }

        }

    }
}
