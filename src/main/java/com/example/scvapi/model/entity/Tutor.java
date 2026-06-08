package com.example.scvapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tutor extends Pessoa {

    @JsonIgnore
    @OneToMany(mappedBy = "tutor")
    private List<Animal> animais;
}