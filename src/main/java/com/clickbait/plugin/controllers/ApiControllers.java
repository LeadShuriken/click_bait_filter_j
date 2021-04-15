package com.clickbait.plugin.controllers;

import java.util.UUID;
import com.clickbait.plugin.dao.User;
import com.clickbait.plugin.security.Role;
import com.clickbait.plugin.services.ApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${api.version}")
public class ApiControllers {
    private final ApiService apiService;

    @Autowired
    public ApiControllers(ApiService userService) {
        this.apiService = userService;
    }

    @RequestMapping(value = "${api.endpoints.clicks_register}", method = RequestMethod.POST)
    public String registerLink() {
        return "Hello Click!";
    }

    @RequestMapping(value = "${api.endpoints.page_segmentation}", method = RequestMethod.POST)
    public String fetchPageSegmentation() {
        // USERS
        System.out.println(apiService.addNewUser(new User(null, "a", "passwordA", Role.USER)));
        System.out.println(apiService.getAllUsers());
        System.out.println(apiService.addNewUser(new User(null, "b", "passwordB", Role.ADMIN)));
        System.out.println(apiService.getAllUsers());
        User a = apiService.getUser("a", "passwordA");
        System.out.println(a);
        System.out.println(apiService.updateUser(a.getUserId(), new User(null, "c", "passwordC", Role.ADMIN)));
        System.out.println(apiService.getAllUsers());
        System.out.println(apiService.deleteUser(a.getUserId()));
        System.out.println(apiService.getAllUsers());
        System.out.println(apiService.addNewUser(new User(null, "b", "passwordB", null)));

        // TABS
        System.out.println(apiService.addNewUser(new User(null, "b", "passwordB", Role.ADMIN)));
        User b = apiService.getUser("b", "passwordB");
        System.out.println(apiService.getAllTabs());
        System.out.println(apiService.getUserTab(UUID.randomUUID(), 1));
        System.out.println(apiService.insertTab(b.getUserId(), "halabalu", 1));
        System.out.println(apiService.insertTab(b.getUserId(), "dungadung", 2));
        System.out.println(apiService.getAllTabs());
        System.out.println(apiService.getUserTab(b.getUserId(), 1));
        System.out.println(apiService.getUserTabs(b.getUserId()));
        return "Hello Page Segmentation!";
    }
}
