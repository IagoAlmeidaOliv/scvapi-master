package com.example.scvapi.api.controller;

import com.example.scvapi.api.dto.EspecieDTO;
import com.example.scvapi.exception.RegraNegocioException;
import com.example.scvapi.model.entity.Especie;
import com.example.scvapi.service.EspecieService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/especies")
@RequiredArgsConstructor
@CrossOrigin
public class EspecieController {

    private final EspecieService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Especie> especies = service.getEspecies();
        return ResponseEntity.ok(especies.stream().map(EspecieDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Especie> especie = service.getEspecieById(id);
        if (!especie.isPresent()) {
            return new ResponseEntity("Espécie não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(especie.map(EspecieDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody EspecieDTO dto) {
        try {
            Especie especie = converter(dto);
            especie = service.salvar(especie);
            return new ResponseEntity(especie, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Especie converter(EspecieDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Especie especie = modelMapper.map(dto, Especie.class);
        return especie;
    }
}