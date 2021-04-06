package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private UserRepository userRepo = mock(UserRepository.class);
    private OrderRepository orderRepo = mock(OrderRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    User user = new User();
    Cart cart = new Cart();
    Item item = new Item();
    @Before
    public void setup(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
        TestUtils.injectObjects(orderController, "cartRepository", cartRepo);

        item.setId(1L);
        item.setName("Round container");
        item.setPrice(BigDecimal.valueOf(2.99));
        cart.addItem(item);

        user.setUsername("test");
        user.setPassword("password");
        user.setCart(cart);

        when(userRepo.findByUsername("test")).thenReturn(user);
    }

    @Test
    public void testSubmitOrder() throws Exception{
        ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());
        UserOrder order = response.getBody();
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertEquals(BigDecimal.valueOf(2.99),order.getTotal());
        Assert.assertTrue(order.getItems().contains(item));
    }


}
