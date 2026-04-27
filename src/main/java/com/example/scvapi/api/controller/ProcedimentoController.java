package com.example.scvapi.api.controller;

import com.example.scvapi.api.dto.ProcedimentoDTO;
import com.example.scvapi.model.entity.Procedimento;
import com.example.scvapi.service.ProcedimentoService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/procedimentos")
public class ProcedimentoController {

    private final ProcedimentoService service;

    public ProcedimentoController(ProcedimentoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProcedimentoDTO>> get() {
        List<Procedimento> procedimentos = service.getProcedimentos();
        List<ProcedimentoDTO> dtos = procedimentos.stream()
                .map(ProcedimentoDTO::create)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity post(@RequestBody ProcedimentoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Procedimento procedimento = modelMapper.map(dto, Procedimento.class);
        Procedimento procedimentoSalvo = service.salvar(procedimento);
        return new ResponseEntity(ProcedimentoDTO.create(procedimentoSalvo), HttpStatus.CREATED);
    }
}