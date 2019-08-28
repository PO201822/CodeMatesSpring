package com.codecool.controller;
import com.codecool.model.Carts;
import com.codecool.model.JWTToken;
import com.codecool.model.OrderDto;
import com.codecool.model.Users;
import com.codecool.repository.CartItemsRepository;
import com.codecool.repository.CartRepository;
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

    @PersistenceContext
    private EntityManager em;

    @PostMapping(path="/addToCart")
    public void addToCart(@RequestBody OrderDto orderDto) {

        String tokenA = orderDto.getToken();
        tokenA = tokenA.replaceAll("\"", "");
        if (jwtTokenServices.validateToken(tokenA)) {
            Authentication auth = jwtTokenServices.parseUserFromTokenInfo(tokenA);
            String name = auth.getName();
            Users user = userRepository.findByName(name)
                    .orElseThrow(() -> new UsernameNotFoundException("Username: " + name + " not found"));

            List<Carts> resultList = em.createNamedQuery("findAvailableCartsByUserId", Carts.class).setParameter("user", user).getResultList();

            if(resultList.size() == 0){
                Carts newCart = cartRepository.save(new Carts(user, 0, false));

            } else {
                //addtocart
            }

        }

    }
}
