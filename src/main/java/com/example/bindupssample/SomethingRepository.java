package com.example.bindupssample;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface SomethingRepository extends JpaRepository<SomethingEntity, String> {
    public SomethingEntity findById(int id);
}
