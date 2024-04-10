package com.skillstorm.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "users")
public class User {

    //Mongo will automatically convert to a BSON
    @Id
    private String id;

    private String firstName;
}