package com.example.scvapi.api.dto;

import com.example.scvapi.model.entity.Raca;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RacaDTO {

    private Long id;
    private String nome;

    private Long idEspecie;
    private String nomeEspecie;

    public static RacaDTO create(Raca raca) {
        ModelMapper modelMapper = new ModelMapper();
        RacaDTO dto = modelMapper.map(raca, RacaDTO.class);

        if (raca.getEspecie() != null) {
            dto.setNomeEspecie(raca.getEspecie().getNome());
        }

        return dto;
    }
}