package com.example.scvapi.api.dto;

import com.example.scvapi.model.entity.Animal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

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

    public static AnimalDTO create(Animal animal) {
        ModelMapper modelMapper = new ModelMapper();
        AnimalDTO dto = modelMapper.map(animal, AnimalDTO.class);

        dto.setTutorNome(animal.getTutor().getNome());
        dto.setRacaNome(animal.getRaca().getNome());

        return dto;
    }

    public Long getIdRaca() {
        return racaId;
    }

    public Long getIdTutor() {
        return tutorId;
    }
}