package com.example.demo.controllers;


import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private CartRepository cartRepo = mock(CartRepository.class);
    private UserRepository userRepo = mock(UserRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);
    User user = new User();
    Item item = new Item();
    Cart cart = new Cart();
    @Before
    public void setup(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        user.setUsername("test");
        user.setPassword("password");
        user.setCart(cart);
        item.setId(1L);
        item.setName("Round container");
        item.setPrice(BigDecimal.valueOf(2.99));

        when(userRepo.findByUsername("test")).thenReturn(user);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));
    }

    @Test
    public void testAddItemToCart(){
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername(user.getUsername());
        cartRequest.setItemId(item.getId());
        cartRequest.setQuantity(1);

        ResponseEntity<Cart> response = cartController.addTocart(cartRequest);

        Assert.assertEquals(200, response.getStatusCodeValue());
        Cart cart = response.getBody();
        Assert.assertNotNull(cart);
        Assert.assertTrue(cart.getItems().contains(item));
    }

    @Test
    public void testRemoveItemToCart(){
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername(user.getUsername());
        cartRequest.setItemId(item.getId());
        cartRequest.setQuantity(1);

        ResponseEntity<Cart> response = cartController.removeFromcart(cartRequest);

        Assert.assertEquals(200, response.getStatusCodeValue());
        Cart cart = response.getBody();
        Assert.assertNotNull(cart);
        Assert.assertTrue(!cart.getItems().contains(item));
    }

}
