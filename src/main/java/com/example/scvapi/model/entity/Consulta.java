package com.example.scvapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dataConsulta;
    private String horaConsulta;
    private String observacoes;

    @ManyToOne
    private Veterinario veterinario;

    @ManyToOne
    private Animal animal;

    @ManyToMany
    @JoinTable(
            name = "ProcedimentosConsultas",
            joinColumns = @JoinColumn(name="idConsulta"),
            inverseJoinColumns = @JoinColumn(name="idProcedimento")
    )
    private List<Procedimento> procedimentos;
}