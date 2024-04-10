package com.skillstorm.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {

    //Mongo will automatically convert to a BSON
    @Id
    private String id;
}