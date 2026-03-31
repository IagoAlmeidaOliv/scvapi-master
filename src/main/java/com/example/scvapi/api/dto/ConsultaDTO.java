package com.example.scvapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaDTO {

    private Long id;
    private Date dataConsulta;
    private String horaConsulta;
    private String observacoes;
    private Long veterinarioId;
    private String veterinarioNome;
    private Long animalId;
    private String animalNome;
    private List<ProcedimentoDTO> procedimentos;
}