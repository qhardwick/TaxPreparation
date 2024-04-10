package com.skillstorm.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillstorm.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    private String id;

    private String firstName;

    public UserDto(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
    }

    @JsonIgnore
    public User getUser() {
        User user = new User();
        user.setId(this.id);
        user.setFirstName(this.firstName);

        return user;
    }
}
