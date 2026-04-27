package com.example.scvapi.api.controller;

import com.example.scvapi.api.dto.RacaDTO;
import com.example.scvapi.model.entity.Raca;
import com.example.scvapi.service.RacaService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/racas")
public class RacaController {

    private final RacaService service;

    public RacaController(RacaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RacaDTO>> get() {
        List<Raca> racas = service.getRacas();
        List<RacaDTO> dtos = racas.stream()
                .map(RacaDTO::create)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Raca> raca = service.getRacaById(id);
        if (!raca.isPresent()) {
            return new ResponseEntity("Raça não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(raca.map(RacaDTO::create));
    }

    @PostMapping
    public ResponseEntity post(@RequestBody RacaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Raca raca = modelMapper.map(dto, Raca.class);
        Raca racaSalva = service.salvar(raca);
        return new ResponseEntity(RacaDTO.create(racaSalva), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        Optional<Raca> raca = service.getRacaById(id);
        if (!raca.isPresent()) {
            return new ResponseEntity("Raça não encontrada", HttpStatus.NOT_FOUND);
        }
        service.excluir(raca.get());
        return ResponseEntity.noContent().build();
    }
}