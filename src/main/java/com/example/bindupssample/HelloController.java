package com.example.bindupssample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    public SomethingRepository somethingRepository;

    @GetMapping("/v1/hello/{id}")
    public SomethingEntity get(@PathVariable(name = "id") int id){
        return somethingRepository.findById(id);
    }

}
