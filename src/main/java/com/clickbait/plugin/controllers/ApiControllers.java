package com.clickbait.plugin.controllers;

import java.util.Arrays;
import java.util.UUID;
import com.clickbait.plugin.dao.User;
import com.clickbait.plugin.security.ApplicationUserRole;
import com.clickbait.plugin.security.ApplicationUserPrivilege;
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
        // USERS ADD/FETCH
        System.out.println(apiService.addNewUser(new User("a", "passwordA", ApplicationUserRole.USER)));
        System.out.println(apiService.getAllUsers());
        System.out.println(apiService.addNewUser(new User("b", "passwordB", ApplicationUserRole.ADMIN)));
        System.out.println(apiService.getAllUsers());
        User a = apiService.getUser("a", "passwordA");
        System.out.println(apiService.isUserActive(a.getUserId()));
        System.out.println(apiService.activateUser(a.getUserId(), false));
        System.out.println(apiService.isUserActive(a.getUserId()));
        System.out.println(apiService.activateUser(a.getUserId(), true));
        System.out.println(apiService.isUserActive(a.getUserId()));
        
        System.out.println(a);
        a = apiService.getUser(a.getUserId());
        System.out.println(a);
        System.out.println(apiService.getAllUsers());

        // USERS UPDATE
        System.out.println(apiService.updateUser(a.getUserId(), new User("c", null)));
        System.out.println(apiService.getUser(a.getUserId()));

        System.out.println(apiService.updateUser(a.getUserId(), new User(null, "c")));
        System.out.println(apiService.getUser(a.getUserId()));

        System.out.println(apiService.updateUser(a.getUserId(), new User("x", "x")));
        System.out.println(apiService.getUser(a.getUserId()));

        System.out.println(apiService.updateUser(a.getUserId(), new User(null, null, ApplicationUserRole.USER)));
        System.out.println(apiService.getUser(a.getUserId()));

        System.out.println(apiService.updateUser(a.getUserId(), new User(null, null, ApplicationUserRole.ADMIN)));
        System.out.println(apiService.getUser(a.getUserId()));

        System.out.println(apiService.removePrivilige(a.getUserId(),
                Arrays.asList(ApplicationUserPrivilege.CLICKS_READ, ApplicationUserPrivilege.DOMAINS_READ)));
        System.out.println(apiService.getUser(a.getUserId()));

        System.out
                .println(apiService.addPrivilige(a.getUserId(), Arrays.asList(ApplicationUserPrivilege.DOMAINS_READ)));
        System.out.println(apiService.getUser(a.getUserId()));

        System.out.println(apiService.updateUser(a.getUserId(),
                new User(null, null, null, Arrays.asList(ApplicationUserPrivilege.CLICKS_READ))));
        System.out.println(apiService.getUser(a.getUserId()));

        System.out.println(apiService.getAllUsers());
        System.out.println(apiService.deleteUser(a.getUserId()));
        System.out.println(apiService.getAllUsers());

        // TABS
        System.out.println(apiService.addNewUser(new User("b", "passwordB")));
        User b = apiService.getUser("b", "passwordB");
        System.out.println(apiService.getAllTabs());
        System.out.println(apiService.getUserTab(UUID.randomUUID(), 1));
        System.out.println(apiService.insertTab(b.getUserId(), "halabalu", 1));
        System.out.println(apiService.insertTab(b.getUserId(), "dungadung", 2));
        System.out.println(apiService.getAllTabs());
        System.out.println(apiService.getUserTab(b.getUserId(), 1));
        System.out.println(apiService.getUserTabs(b.getUserId()));

        // CLICKS
        UUID c = apiService.addNewUser(new User("b", "passwordBdasda", ApplicationUserRole.ADMIN));
        System.out.println(apiService.insertTab(c, "halabalu", 1));
        System.out.println(apiService.getAllTabs());
        System.out.println(apiService.getAllClicks());
        System.out.println(apiService.addClick(c, "halabalu", "halabalu_first_link"));
        System.out.println(apiService.getAllClicks());
        System.out.println(apiService.insertTab(c, "halabalusds", 1));
        System.out.println(apiService.addClick(c, "halabalusds", "halabalu_first_link"));
        System.out.println(apiService.addClick(c, "halabalusds", "halabalu_first_link"));
        System.out.println(apiService.addClick(c, "halabalusds", "halabalu_first_link"));
        System.out.println(apiService.addClick(c, "halabalusds", "halabalu_first_link"));
        System.out.println(apiService.getAllClicks());

        return "Hello Page Segmentation!";
    }
}
