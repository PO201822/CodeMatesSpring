package com.codecool.controller;

import com.codecool.dto.CartItemDto;
import com.codecool.dto.CartOrderDto;
import com.codecool.dto.OrderDto;
import com.codecool.entity.*;
import com.codecool.repository.*;
import com.codecool.service.Simple.SimpleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
public class CartController {

    @Autowired
    private SimpleUserService simpleUserService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @PersistenceContext
    private EntityManager em;

    @PostMapping(path = "/addToCart")
    public void addToCart(@RequestBody OrderDto orderDto) {
        int productId = orderDto.getProductId();
        int quantity = orderDto.getQuantity();
        Products products = productsRepository.findById(productId);
        Users user = userRepository.findByName(simpleUserService.getCurrentUser());

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
        Users user = userRepository.findByName(simpleUserService.getCurrentUser());
        if (cartRepository.findAllByUserId(user.getId()).size() == 0) {
            //no cart yet
            return null;
        }

        List<Carts> cartsList = em.createNamedQuery("findAvailableCartsByUserId", Carts.class)
                .setParameter("user", user).getResultList();

        if (cartsList.size() == 0 || cartsList.get(0).getCartItems().size() == 0) {
            //none available
            return null;
        } else {
            return em.createNamedQuery("findCartItemsByCartId", CartItems.class)
                    .setParameter("cart_id", cartsList.get(0).getId()).getResultList();
        }
    }

    @PutMapping(path = "/checkout")
    public @ResponseBody
    void checkoutCart() {
        Users user = userRepository.findByName(simpleUserService.getCurrentUser());
        List<Carts> cartsList = em.createNamedQuery("findAvailableCartsByUserId", Carts.class)
                .setParameter("user", user).getResultList();

        cartsList.get(0).setCheckedOut(true);
        cartsList.get(0).setCheckout_date(LocalDateTime.now());
        cartRepository.save(cartsList.get(0));
    }

    @PutMapping(path = "/updateCartItemQuantity")
    public @ResponseBody
    void updateCartItems(@RequestBody CartItemDto cartItemDto) {
        Users user = userRepository.findByName(simpleUserService.getCurrentUser());
        CartItems cartItems = cartItemsRepository.findById(cartItemDto.getCartItemId());
        cartItems.setQuantity(cartItemDto.getQuantity());
        cartItemsRepository.save(cartItems);
    }

    @DeleteMapping(path = "/deleteItem")
    @Transactional
    public @ResponseBody
    void deleteFromCart(@RequestParam int productId) {
        Products byId = productsRepository.findById(productId);
        Users user = userRepository.findByName(simpleUserService.getCurrentUser());
        List<Carts> cartsList = em.createNamedQuery("findAvailableCartsByUserId", Carts.class)
                .setParameter("user", user).getResultList();

        em.createNamedQuery("deleteProductFromCart").setParameter("product", byId).setParameter("cart", cartsList.get(0)).executeUpdate();
    }

    @GetMapping(path = "/getOrders")
    public List<CartOrderDto> getOrders() {
        Users user = userRepository.findByName(simpleUserService.getCurrentUser());
        List<CartOrderDto> cartOrderDtos = new ArrayList<>();
        List<Carts> carts = cartRepository.findAllByUserIdAndCheckedOut(user.getId(),true);
        if (carts.size() == 0){
            return null;
        }

        for (Carts cart : carts){
            boolean isCompleted = false;
            LocalDateTime completionDate = null;
            int quantity = 0;
            int total = 0;
            String status = "Waiting for pick up";
            Users costumer = new Users(cart.getUser().getName(), cart.getUser().getLocation(), cart.getUser().getAddress());
            for (CartItems cartItem : cart.getCartItems() ){
                quantity+= cartItem.getQuantity();
                total+= cartItem.getQuantity()* cartItem.getPrice();
            }
            if (cart.isPickedup()){
                Orders order = ordersRepository.findByCartId(cart.getId());
                isCompleted = order.isComplete();
                status = isCompleted? "Completed" : "Delivering";
                completionDate = order.getComplition_date();

            }
            cartOrderDtos.add(new CartOrderDto(cart, isCompleted, completionDate,cart.getCartItems(),quantity,total,status,costumer));
        }
        return cartOrderDtos;
    }
}
