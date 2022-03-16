package com.fcs.marathonbademo.service;

import com.fcs.marathonbademo.dto.UserDTO;
import com.fcs.marathonbademo.entity.User;
import com.fcs.marathonbademo.repository.UserRepository;
import com.fcs.marathonbademo.util.RESTResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public ResponseEntity<?> getAllUser() {
        List<User> all = userRepository.findAll();
        List<UserDTO> listUserDto = all.stream().map(UserDTO::new).collect(Collectors.toList());
        HashMap<String, Object> restResp = new RESTResponse.Success()
                .setMessage("Fetched success")
                .setStatus(HttpStatus.OK.value())
                .setData(listUserDto).build();
        return ResponseEntity.ok().body(restResp);
    }
}
