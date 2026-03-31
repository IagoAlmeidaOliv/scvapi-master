package com.example.scvapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimalDTO {

    private Long id;
    private String nome;
    private Date dataNascimento;
    private char sexo;
    private char castrado;
    private String observacoes;
    private byte[] foto;
    private Long tutorId;
    private String tutorNome;
    private Long racaId;
    private String racaNome;
}