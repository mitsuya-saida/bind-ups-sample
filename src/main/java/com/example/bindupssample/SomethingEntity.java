package com.example.bindupssample;

import lombok.Data;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "something")
@Data
public class SomethingEntity {

    @Id
    private int id;

    private String name;
}
