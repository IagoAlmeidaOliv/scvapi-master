package com.example.scvapi.api.dto;

import com.example.scvapi.model.entity.Consulta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

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
    private Long idVeterinario;
    private String nomeVeterinario;
    private Long idAnimal;
    private String nomeAnimal;

    private List<ProcedimentoDTO> procedimentos;

    public static ConsultaDTO create(Consulta consulta) {
        ModelMapper modelMapper = new ModelMapper();
        ConsultaDTO dto = modelMapper.map(consulta, ConsultaDTO.class);


        if (consulta.getVeterinario() != null) {
            dto.setNomeVeterinario(consulta.getVeterinario().getNome());
        }

        if (consulta.getAnimal() != null) {
            dto.setNomeAnimal(consulta.getAnimal().getNome());
        }

        return dto;
    }
}