package com.example.scvapi.api.controller;

import com.example.scvapi.api.dto.VeterinarioDTO;
import com.example.scvapi.exception.RegraNegocioException;
import com.example.scvapi.model.entity.Veterinario;
import com.example.scvapi.service.VeterinarioService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/veterinarios")
@RequiredArgsConstructor
@CrossOrigin
public class VeterinarioController {

    private final VeterinarioService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Veterinario> veterinarios = service.getVeterinarios();
        return ResponseEntity.ok(veterinarios.stream().map(VeterinarioDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Veterinario> veterinario = service.getVeterinarioById(id);
        if (!veterinario.isPresent()) {
            return new ResponseEntity("Veterinário não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(veterinario.map(VeterinarioDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody VeterinarioDTO dto) {
        try {
            Veterinario veterinario = converter(dto);
            veterinario = service.salvar(veterinario);
            return new ResponseEntity(veterinario, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Veterinario converter(VeterinarioDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Veterinario veterinario = modelMapper.map(dto, Veterinario.class);
        return veterinario;
    }
}