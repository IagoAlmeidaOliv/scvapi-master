package com.example.scvapi.api.dto;

import com.example.scvapi.model.entity.Veterinario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VeterinarioDTO extends PessoaDTO {

    private String crmv;

    public static VeterinarioDTO create(Veterinario veterinario) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(veterinario, VeterinarioDTO.class);
    }
}