package com.example.scvapi.api.dto;

import com.example.scvapi.model.entity.Estagio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstagioDTO {

    private Long id;
    private String dataInicio;
    private String dataFim;
    private Integer cargaHoraria;
    private Integer tipoEstagio;
    private String planoAtividades;
    private Long idAluno;
    private String nomeAluno;
    private Long idConcedente;
    private String nomeConcedente;

    public static EstagioDTO create(Estagio estagio) {
        ModelMapper modelMapper = new ModelMapper();
        EstagioDTO dto = modelMapper.map(estagio, EstagioDTO.class);
        dto.nomeAluno = estagio.getAluno().getNome();
        dto.nomeConcedente = estagio.getConcedente().getNome();
        return dto;
    }
}
