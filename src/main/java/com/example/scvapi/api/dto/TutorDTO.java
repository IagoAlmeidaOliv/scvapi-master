package com.example.scvapi.api.dto;

import com.example.scvapi.model.entity.Tutor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorDTO extends PessoaDTO {

    private List<AnimalDTO> animais;

    public static TutorDTO create(Tutor tutor) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(tutor, TutorDTO.class);
    }
}