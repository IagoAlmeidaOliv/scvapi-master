package com.example.scvapi.api.controller;

import com.example.scvapi.api.dto.VeterinarioDTO;
import com.example.scvapi.model.entity.Veterinario;
import com.example.scvapi.service.VeterinarioService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/veterinarios")
public class VeterinarioController {

    private final VeterinarioService service;

    public VeterinarioController(VeterinarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<VeterinarioDTO>> get() {
        List<Veterinario> veterinarios = service.getVeterinarios();
        List<VeterinarioDTO> dtos = veterinarios.stream()
                .map(VeterinarioDTO::create)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Veterinario> veterinario = service.getVeterinarioById(id);
        if (!veterinario.isPresent()) {
            return new ResponseEntity("Veterinário não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(veterinario.map(VeterinarioDTO::create));
    }

    @PostMapping
    public ResponseEntity post(@RequestBody VeterinarioDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Veterinario veterinario = modelMapper.map(dto, Veterinario.class);
        Veterinario veterinarioSalvo = service.salvar(veterinario);
        return new ResponseEntity(VeterinarioDTO.create(veterinarioSalvo), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        Optional<Veterinario> veterinario = service.getVeterinarioById(id);
        if (!veterinario.isPresent()) {
            return new ResponseEntity("Veterinário não encontrado", HttpStatus.NOT_FOUND);
        }
        service.excluir(veterinario.get());
        return ResponseEntity.noContent().build();
    }
}