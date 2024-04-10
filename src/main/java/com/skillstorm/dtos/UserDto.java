package com.skillstorm.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillstorm.entities.User;
import lombok.Data;

@Data
public class UserDto {

    private String id;

    public UserDto(User user) {
        this.id = user.getId();
    }

    @JsonIgnore
    public User getUser() {
        User user = new User();
        user.setId(this.id);

        return user;
    }
}
