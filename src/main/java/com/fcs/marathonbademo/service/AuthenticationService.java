package com.fcs.marathonbademo.service;


import com.fcs.marathonbademo.dto.AuthDTO;
import com.fcs.marathonbademo.dto.UserDTO;
import com.fcs.marathonbademo.entity.User;
import com.fcs.marathonbademo.repository.GameCharacterRepository;
import com.fcs.marathonbademo.repository.UserRepository;
import com.fcs.marathonbademo.util.EtherUtil;
import com.fcs.marathonbademo.util.JwtUtil;
import com.fcs.marathonbademo.util.RESTResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {
    private final UserRepository userRepository;
    private final GameCharacterRepository gameCharacterRepository;

    //get signature, messsage, public key if public key not exist -> register new user
    public ResponseEntity<?> loginAndRegister(AuthDTO authDTO) {
        HashMap<String, Object> restResponse;
        String publicKey = EtherUtil.recoverPublicKey(authDTO.getAddress(), authDTO.getMessage(), authDTO.getSignature());
        if (publicKey == null) {
            restResponse = new RESTResponse.CustomError()
                    .setCode(HttpStatus.BAD_REQUEST.value())
                    .setMessage("Signature invalid").build();
            return ResponseEntity.badRequest().body(restResponse);
        }
        try {
            Optional<User> userByAddress = userRepository.findByAddress(publicKey);
            User toAuthenticateUser = null;
            if (!userByAddress.isPresent()) {
                //create new user
                User newUser = new User();
                newUser.setAddress(publicKey);
                newUser.setCreatedAt(new Date());
                newUser.setUpdatedAt(new Date());
                newUser.setStatus(1);
                //save
                toAuthenticateUser = userRepository.save(newUser);

            } else {
                toAuthenticateUser = userByAddress.get();
            }
            //gen jwt token
            String accessToken = JwtUtil.generateToken(toAuthenticateUser.getAddress(),
                    null,
                    JwtUtil.ONE_DAY * 7);

            String refreshToken = JwtUtil.generateToken(toAuthenticateUser.getAddress(),
                    null,
                    JwtUtil.ONE_DAY * 14);
            AuthDTO.AuthSuccessDTO authSuccessDTO = new AuthDTO.AuthSuccessDTO(accessToken, refreshToken, new UserDTO(toAuthenticateUser));
            restResponse = new RESTResponse.Success()
                    .setMessage("Login success")
                    .setStatus(HttpStatus.OK.value())
                    .setData(authSuccessDTO).build();
            return ResponseEntity.ok().body(restResponse);

        } catch (Exception exception) {
            restResponse = new RESTResponse.CustomError()
                    .setCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .setMessage(exception.getMessage()).build();
            return ResponseEntity.internalServerError().body(restResponse);
        }
    }
}
