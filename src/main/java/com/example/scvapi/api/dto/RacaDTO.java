package com.example.scvapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RacaDTO {

    private Long id;
    private String nome;
    private Long especieId;
    private String especieNome;
}