package com.fcs.marathonbademo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/characters")
@CrossOrigin
public class CharacterController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getAll() {
        return "all";
    }
}
