package com.clickbait.plugin.controllers;

import java.util.UUID;

import com.clickbait.plugin.security.AuthenticatedUser;
import com.clickbait.plugin.services.ApplicationDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${api.version}")
public class ClickBaitApiController {

    @Autowired
    private ApplicationDataService applicationDataService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER') and hasAuthority('CLICKS_WRITE')")
    @PostMapping(value = "${api.clicks_register}")
    public String registerLink() {
        // 1. Get domain from body.domain by extractiong hostname
        // 2. Fetch all linkst for domain
        // 3. If by some freak accident there is no such domain aka land second
        // Register with name link score (tflow)
        // 4. Else register click score (tflow) or increment and rescore
        // 5. Insert in clicks
        UUID a = ((AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUserId();
        return a.toString();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER') and hasAuthority('DOMAINS_READ')")
    @PostMapping(value = "${api.page_segmentation}")
    public String fetchPageSegmentation() {
        // 1. Check for page in request
        // 1.1. Passed from a new tab aka page
            // 1.1.1 Get host name aka domain
            // 1.1.2 Tab not in user tabs - add with domain name and id 
        // 2.1. Passed from tab id aka tabId
            // 2.1.1 Find tab for the user
        // 3. Fetch Domain with links
        // 4. Check if links are in request
        // 5. In Request Find one's not registered in fetched domain
        // 6. If any register and score with tflow server
        // 7. If domain has registered links plus ones' just registered
        // 8. URL: Score Map 
        return "Hello Page Segmentation!";
    }
}
