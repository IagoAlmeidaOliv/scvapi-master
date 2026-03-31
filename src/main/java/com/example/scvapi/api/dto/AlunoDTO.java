package com.example.scvapi.api.dto;

import com.example.scvapi.model.entity.Aluno;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoDTO {

    private Long id;
    private Integer matricula;
    private String nome;
    private String cpf;
    private String email;
    private String celular;
    private Long idCurso;

    public static AlunoDTO create(Aluno aluno) {
        ModelMapper modelMapper = new ModelMapper();
        AlunoDTO dto = modelMapper.map(aluno, AlunoDTO.class);
        return dto;
    }

}
