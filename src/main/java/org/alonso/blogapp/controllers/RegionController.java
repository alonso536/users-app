package org.alonso.blogapp.controllers;

import java.util.List;

import org.alonso.blogapp.models.entities.Region;
import org.alonso.blogapp.models.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/regions")
public class RegionController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Region> regions() {
        return userService.findRegions();
    }
}
