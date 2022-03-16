package com.fcs.marathonbademo.controller;

import com.fcs.marathonbademo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUsers() {
        return userService.getAllUser();
    }
}
