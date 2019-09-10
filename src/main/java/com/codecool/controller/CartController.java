package com.codecool.controller;

import com.codecool.dto.CartItemDto;
import com.codecool.dto.OrderDto;
import com.codecool.entity.CartItems;
import com.codecool.entity.Carts;
import com.codecool.entity.Products;
import com.codecool.entity.Users;
import com.codecool.repository.CartItemsRepository;
import com.codecool.repository.CartRepository;
import com.codecool.repository.ProductsRepository;
import com.codecool.repository.UserRepository;
import com.codecool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
            CartItems save = cartItemsRepository.save(new CartItems(newCart, products, quantity, products.getPrice()));
        } else {
            boolean isInCart = false;

            List<CartItems> cartItems = cartsList.get(0).getCartItems();

            for (CartItems c : cartItems) {
                if (c.getProduct().equals(products)) {
                    c.setQuantity(quantity += c.getQuantity());
                    cartItemsRepository.save(c);
                    isInCart = true;
                }
            }
            if (!isInCart) {
                CartItems save = cartItemsRepository.save(new CartItems(cartsList.get(0), products, quantity, products.getPrice()));
            }
        }

    }

    @GetMapping(path = "/myCart")
    public List<CartItems> getMyCart() {
        Users user = userRepository.findByName(userService.currentUser());
        List<Carts> cartsList = em.createNamedQuery("findAvailableCartsByUserId", Carts.class)
                .setParameter("user", user).getResultList();

        if (cartsList.get(0).getCartItems().size() == 0) {
            //the cart is empty
            return null;
        } else {
            return em.createNamedQuery("findCartItemsByCartId", CartItems.class)
                    .setParameter("cart_id", cartsList.get(0).getId()).getResultList();
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

    @PutMapping(path = "/updateCartItemQuantity")
    public @ResponseBody
    void updateCartItems(@RequestBody CartItemDto cartItemDto) {
        Users user = userRepository.findByName(userService.currentUser());
        CartItems cartItems = cartItemsRepository.findById(cartItemDto.getCartItemId());
        cartItems.setQuantity(cartItemDto.getQuantity());
        cartItemsRepository.save(cartItems);
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
