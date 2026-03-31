package com.example.scvapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorDTO extends PessoaDTO {

    private List<AnimalDTO> animais;

}