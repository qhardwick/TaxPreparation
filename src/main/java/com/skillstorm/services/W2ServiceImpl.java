package com.skillstorm.services;

import com.skillstorm.dtos.W2Dto;
import com.skillstorm.entities.W2;
import com.skillstorm.exceptions.W2NotFoundException;
import com.skillstorm.repositories.W2Repository;
import com.skillstorm.utils.SystemMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@PropertySource("classpath:SystemMessages.properties")
public class W2ServiceImpl implements W2Service {

    private final W2Repository w2Repository;
    private final Environment environment;

    @Autowired
    public W2ServiceImpl(W2Repository w2Repository, Environment environment) {
        this.w2Repository = w2Repository;
        this.environment = environment;
    }

    @Override
    public W2Dto addW2(W2Dto newW2) {
        return new W2Dto(w2Repository.save(newW2.getW2()));
    }

    @Override
    public W2Dto findW2ById(int id) {
        Optional<W2> w2Optional = w2Repository.findById(id);
        if(!w2Optional.isPresent()) {
            throw new W2NotFoundException(environment.getProperty(SystemMessages.W2_NOT_FOUND.toString()));
        }
        return new W2Dto(w2Optional.get());
    }

    @Override
    public W2Dto updateW2ById(int id, W2Dto updatedW2) {
        findW2ById(id);
        updatedW2.setId(id);
        return new W2Dto(w2Repository.save(updatedW2.getW2()));
    }

    @Override
    public void deleteW2ById(int id) {
        findW2ById(id);
        w2Repository.deleteById(id);
    }
}
