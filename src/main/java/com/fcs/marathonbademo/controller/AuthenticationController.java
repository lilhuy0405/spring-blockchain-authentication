package com.fcs.marathonbademo.controller;

import com.fcs.marathonbademo.dto.AuthDTO;
import com.fcs.marathonbademo.service.AuthenticationService;
import com.fcs.marathonbademo.util.RestHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> loginRegister(@RequestBody @Valid AuthDTO authDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return RestHelper.getValidationErrorsResponse(bindingResult, "auth failed");
        }
        return authenticationService.loginAndRegister(authDTO);
    }

}
