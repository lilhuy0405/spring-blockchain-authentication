package com.fcs.marathonbademo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthDTO {
    @NotBlank
    private String message;
    @NotBlank
    private String signature;
    @NotBlank
    private String address;

    @AllArgsConstructor
    @Data
    public static class AuthSuccessDTO {
        private String accessToken;
        private String refreshToken;
        private UserDTO user;
    }
}
