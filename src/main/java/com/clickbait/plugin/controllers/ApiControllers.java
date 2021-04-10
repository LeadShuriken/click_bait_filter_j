package com.clickbait.plugin.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${api.version}")
public class ApiControllers {

    @RequestMapping(value = "${api.endpoints.clicks_register}", method = RequestMethod.POST)
    public String registerLink() {
        return "Hello Click!";
    }

    @RequestMapping(value = "${api.endpoints.page_segmentation}", method = RequestMethod.POST)
    public String fetchPageSegmentation() {
        return "Hello Page Segmentation!";
    }
}
