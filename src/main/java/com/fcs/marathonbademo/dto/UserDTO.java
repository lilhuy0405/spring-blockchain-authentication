package com.fcs.marathonbademo.dto;

import com.fcs.marathonbademo.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserDTO {
    private int id;
    private String address;
    private String nickname;
    private int level;
    private int status;
    private Date createdAt;
    private Date updatedAt;
    private String role;

    public UserDTO(User user) {
        this.id = user.getId();
        this.address = user.getAddress();
        this.nickname = user.getNickname();
        this.level = user.getLevel();
        this.status = user.getStatus();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getCreatedAt();
        this.role = user.getRole().getName();
    }
}
