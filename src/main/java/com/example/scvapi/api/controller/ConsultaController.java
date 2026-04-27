package com.example.scvapi.api.controller;

import com.example.scvapi.api.dto.ConsultaDTO;
import com.example.scvapi.model.entity.Consulta;
import com.example.scvapi.service.ConsultaService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    private final ConsultaService service;

    public ConsultaController(ConsultaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ConsultaDTO>> get() {
        List<Consulta> consultas = service.getConsultas();
        List<ConsultaDTO> dtos = consultas.stream()
                .map(ConsultaDTO::create)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Consulta> consulta = service.getConsultaById(id);
        if (!consulta.isPresent()) {
            return new ResponseEntity("Consulta não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(consulta.map(ConsultaDTO::create));
    }

    @PostMapping
    public ResponseEntity post(@RequestBody ConsultaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Consulta consulta = modelMapper.map(dto, Consulta.class);
        Consulta consultaSalva = service.salvar(consulta);
        return new ResponseEntity(ConsultaDTO.create(consultaSalva), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        Optional<Consulta> consulta = service.getConsultaById(id);
        if (!consulta.isPresent()) {
            return new ResponseEntity("Consulta não encontrada", HttpStatus.NOT_FOUND);
        }
        service.excluir(consulta.get());
        return ResponseEntity.noContent().build();
    }
}