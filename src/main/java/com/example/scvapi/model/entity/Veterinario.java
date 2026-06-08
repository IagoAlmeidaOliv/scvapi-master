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
public class Veterinario extends Pessoa {

    private String crmv;

    @JsonIgnore
    @OneToMany(mappedBy = "veterinario")
    private List<Consulta> consultas;
}