package com.example.scvapi.api.controller;

import com.example.scvapi.api.dto.EspecieDTO;
import com.example.scvapi.model.entity.Especie;
import com.example.scvapi.service.EspecieService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/especies")
public class EspecieController {

    private final EspecieService service;

    public EspecieController(EspecieService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<EspecieDTO>> get() {
        List<Especie> especies = service.getEspecies();
        List<EspecieDTO> dtos = especies.stream()
                .map(EspecieDTO::create)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Especie> especie = service.getEspecieById(id);
        if (!especie.isPresent()) {
            return new ResponseEntity("Espécie não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(especie.map(EspecieDTO::create));
    }

    @PostMapping
    public ResponseEntity post(@RequestBody EspecieDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Especie especie = modelMapper.map(dto, Especie.class);
        Especie especieSalva = service.salvar(especie);
        return new ResponseEntity(EspecieDTO.create(especieSalva), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        Optional<Especie> especie = service.getEspecieById(id);
        if (!especie.isPresent()) {
            return new ResponseEntity("Espécie não encontrada", HttpStatus.NOT_FOUND);
        }
        service.excluir(especie.get());
        return ResponseEntity.noContent().build();
    }
}