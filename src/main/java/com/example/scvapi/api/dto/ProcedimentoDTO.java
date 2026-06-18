package com.example.scvapi.api.dto;

import com.example.scvapi.model.entity.Procedimento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcedimentoDTO {

    private Long id;
    private String nome;
    private Double custo;

    public static ProcedimentoDTO create(Procedimento procedimento) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(procedimento, ProcedimentoDTO.class);
    }
}