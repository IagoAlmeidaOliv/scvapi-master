package com.example.scvapi.api.controller;

import com.example.scvapi.api.dto.TutorDTO;
import com.example.scvapi.exception.RegraNegocioException;
import com.example.scvapi.model.entity.Tutor;
import com.example.scvapi.service.TutorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tutores")
@RequiredArgsConstructor
@CrossOrigin
public class TutorController {

    private final TutorService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Tutor> tutores = service.getTutores();
        return ResponseEntity.ok(tutores.stream().map(TutorDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Tutor> tutor = service.getTutorById(id);
        if (!tutor.isPresent()) {
            return new ResponseEntity("Tutor não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tutor.map(TutorDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody TutorDTO dto) {
        try {
            Tutor tutor = converter(dto);
            tutor = service.salvar(tutor);
            return new ResponseEntity(tutor, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody TutorDTO dto) {
        if (!service.getTutorById(id).isPresent()) {
            return new ResponseEntity("Tutor não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Tutor tutor = converter(dto);
            tutor.setId(id);
            service.salvar(tutor);
            return ResponseEntity.ok(tutor);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Tutor converter(TutorDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Tutor.class);
    }

    @GetMapping("/buscar")
    public ResponseEntity buscarTutoresPorNome(@RequestParam("nome") String nome) {
        List<Tutor> tutores = service.buscarPorNome(nome);
        return ResponseEntity.ok(tutores.stream().map(TutorDTO::create).collect(Collectors.toList()));
    }
}