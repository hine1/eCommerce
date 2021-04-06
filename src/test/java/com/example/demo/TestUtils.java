package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class TestUtils {
    public static void injectObjects(Object target, String fieldName, Object toInject){
        boolean wasPrivate = false;

        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            if (f.get(target) == null){
                f.setAccessible(true);
                wasPrivate = true;
            }
            f.set(target, toInject);
            if (wasPrivate){
                f.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static CreateUserRequest createUserRequest(){
        CreateUserRequest user = new CreateUserRequest();
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setConfirmPassword("testPassword");
        return user;
    }

    public static User createUser(){
        User user = new User();
        user.setUsername("test");
        user.setPassword("testPassword");
        return user;
    }
    public static Item createItem(){
        Item item = new Item();
        item.setId(1L);
        item.setName("Round container");
        item.setDescription("A widget that is round");
        item.setPrice(new BigDecimal(1.99));
        return item;
    }


}
