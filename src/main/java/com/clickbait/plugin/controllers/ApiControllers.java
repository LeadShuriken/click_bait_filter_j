package com.clickbait.plugin.controllers;

import com.clickbait.plugin.dao.Users;
import com.clickbait.plugin.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${api.version}")
public class ApiControllers {
    private final UserService userService;

    @Autowired
    public ApiControllers(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "${api.endpoints.clicks_register}", method = RequestMethod.POST)
    public String registerLink() {
        return "Hello Click!";
    }

    @RequestMapping(value = "${api.endpoints.page_segmentation}", method = RequestMethod.POST)
    public String fetchPageSegmentation() {
        // HAPPY
        userService.addNewUser(new Users(null, "a", "passwordA"));
        System.out.println(userService.getAllUsers());
        userService.addNewUser(new Users(null, "b", "passwordB"));
        System.out.println(userService.getAllUsers());
        Users a = userService.getUser("a", "passwordA");
        System.out.println(a);
        userService.updateUser(a.getUserId(), new Users(null, "c", "passwordC"));
        System.out.println(userService.getAllUsers());
        userService.deleteUser(a.getUserId());
        System.out.println(userService.getAllUsers());

        // SAD
        try {
            Users c = new Users(null, "b", "passwordB");
            userService.addNewUser(c);
        } catch (Exception e) {
            throw e;
        }

        return "Hello Page Segmentation!";
    }
}
