package com.example.scvapi.api.dto;

import com.example.scvapi.model.entity.Animal;
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
public class AnimalDTO {

    private Long id;
    private String nome;
    private Date dataNascimento;
    private char sexo;
    private char castrado;
    private String observações;
    private byte[] foto;
    private Long idTutor;
    private Long idRaca;
    private List<Consulta> consultas;

    public static AnimalDTO create(Animal animal) {
        ModelMapper modelMapper = new ModelMapper();
        AnimalDTO dto = modelMapper.map(animal, AnimalDTO.class);
        return dto;
    }

}