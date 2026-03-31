package com.example.scvapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcedimentoDTO {

    private Long id;
    private String nome;
    private Double valor;
}