package com.skillstorm.dtos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.skillstorm.entities.User;
import com.skillstorm.validations.AddUserGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
// Used to prevent infinite recursion when serializing the UserDto object to JSON:
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserDto {

    private int id;

    @NotEmpty(message = "{user.firstName.must}")
    @Size(min = 2, message = "{user.firstName.size}")
    @Pattern(regexp = "[a-zA-Z]+", message = "{user.firstName.pattern}")
    private String firstName;

    @NotEmpty(message = "{user.lastName.must}")
    @Size(min = 2, message = "{user.lastName.size}")
    @Pattern(regexp = "[a-zA-Z]+", message = "{user.lastName.pattern}")
    private String lastName;

    @NotEmpty(groups = {AddUserGroup.class, Default.class}, message = "{user.email.must}")
    @Email(groups = {AddUserGroup.class, Default.class}, message = "{user.email.valid}")
    private String email;

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(groups = {AddUserGroup.class}, message = "{user.password.must}")
    private String password;

    @NotEmpty(message = "{user.address.must}")
    private String address;

    @NotEmpty(message = "{user.phoneNumber.must}")
    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$", message = "{user.phoneNumber.valid}")
    private String phoneNumber;

    @NotEmpty(message = "{user.ssn.must}")
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{4}$", message = "{user.ssn.valid}")
    private String ssn;

    private List<W2Dto> w2s;

    private String role;

    public UserDto() {
                w2s = new ArrayList<>(3);
                role = "USER";
    }

    public UserDto(User user) {
        this();
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.address = user.getAddress();
        this.phoneNumber = user.getPhoneNumber();
        this.ssn = user.getSsn();
        this.w2s = user.getW2s().stream().map(W2Dto::new).collect(Collectors.toList());
        this.role = user.getRole();
    }

    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    @JsonIgnore
    public User getUser() {
        User user = new User();
        user.setId(this.id);
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setEmail(this.email);
        user.setUsername(this.username);
        user.setPassword(this.password);
        user.setAddress(this.address);
        user.setPhoneNumber(this.phoneNumber);
        user.setSsn(this.ssn);
        user.setW2s(this.w2s.stream().map(W2Dto::getW2).collect(Collectors.toList()));
        user.setRole(this.role);

        return user;
    }
}
