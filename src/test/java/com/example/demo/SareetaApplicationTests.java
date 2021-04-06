package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SareetaApplicationTests {
    @Autowired
    private UserController userController;

    @Autowired
    private CartController cartController;

    @Autowired
    private OrderController orderController;

    User user = new User();


    @Test
    public void testFindUserByIdOrUsername() throws Exception{
        //Create a new user
        CreateUserRequest userDTO = createUserDTO("username1");
        User user = userController.createUser(userDTO).getBody();
        Assert.assertNotNull(user);

        //Test finding user by id
        ResponseEntity<User> response = userController.findById(user.getId());
        User foundUser = response.getBody();
        Assert.assertNotNull(foundUser);
        Assert.assertEquals(user.getId(), foundUser.getId());

        //Test finding user by username
        response = userController.findByUserName(user.getUsername());
        foundUser = response.getBody();
        Assert.assertNotNull(foundUser);
        Assert.assertEquals(user.getUsername(), foundUser.getUsername());
    }

    @Test
    public void testAddAndRemoveItemInCart() throws Exception{
        //Create a new user
        CreateUserRequest userDTO = createUserDTO("username2");
        User user = userController.createUser(userDTO).getBody();
        Assert.assertNotNull(user);

        Item item = createItem();

        //Test add item to user's cart
        ModifyCartRequest cartDTO = new ModifyCartRequest();
        cartDTO.setUsername(user.getUsername());
        cartDTO.setItemId(item.getId());
        cartDTO.setQuantity(1);
        ResponseEntity<Cart> responseCart = cartController.addTocart(cartDTO);
        Cart cart = responseCart.getBody();
        Assert.assertEquals(200, responseCart.getStatusCodeValue());
        Assert.assertEquals(1, cart.getItems().size());
        Assert.assertEquals(cartDTO.getUsername(), cart.getUser().getUsername());

        //Test remove item from user's cart
        cartDTO.setQuantity(1);
        responseCart = cartController.removeFromcart(cartDTO);
        cart = responseCart.getBody();
        Assert.assertEquals(200, responseCart.getStatusCodeValue());
        Assert.assertEquals(0, cart.getItems().size());
    }
    @Test
    public void testViewOrderHistory() throws Exception{
        //Create a new user
        CreateUserRequest userDTO = createUserDTO("username3");
        User user = userController.createUser(userDTO).getBody();
        Assert.assertNotNull(user);

        List<UserOrder> ordersList = orderController.getOrdersForUser(user.getUsername()).getBody();
        Assert.assertEquals(0, ordersList.size());

        //Add item to cart and submit order
        Item item = createItem();
        ModifyCartRequest cartDTO = new ModifyCartRequest();
        cartDTO.setUsername(user.getUsername());
        cartDTO.setItemId(item.getId());
        cartDTO.setQuantity(1);
        cartController.addTocart(cartDTO);
        orderController.submit(user.getUsername());

        ordersList = orderController.getOrdersForUser(user.getUsername()).getBody();
        Assert.assertEquals(1, ordersList.size());
    }

    public CreateUserRequest createUserDTO(String username){
        CreateUserRequest user = new CreateUserRequest();
        user.setUsername(username);
        user.setPassword("testPassword");
        user.setConfirmPassword("testPassword");
        return user;
    }

    public Item createItem(){
        Item item = new Item();
        item.setId(1L);
        item.setName("Round container");
        item.setPrice(BigDecimal.valueOf(3.99));
        return item;
    }
}
