package com.example.scvapi.api.controller;

import com.example.scvapi.api.dto.TutorDTO;
import com.example.scvapi.model.entity.Tutor;
import com.example.scvapi.service.TutorService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tutores")
public class TutorController {

    private final TutorService service;

    public TutorController(TutorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TutorDTO>> get() {
        List<Tutor> tutores = service.getTutores();
        List<TutorDTO> dtos = tutores.stream()
                .map(TutorDTO::create)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Tutor> tutor = service.getTutorById(id);
        if (!tutor.isPresent()) {
            return new ResponseEntity("Tutor não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tutor.map(TutorDTO::create));
    }

    @PostMapping
    public ResponseEntity post(@RequestBody TutorDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Tutor tutor = modelMapper.map(dto, Tutor.class);
        Tutor tutorSalvo = service.salvar(tutor);
        return new ResponseEntity(TutorDTO.create(tutorSalvo), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        Optional<Tutor> tutor = service.getTutorById(id);
        if (!tutor.isPresent()) {
            return new ResponseEntity("Tutor não encontrado", HttpStatus.NOT_FOUND);
        }
        service.excluir(tutor.get());
        return ResponseEntity.noContent().build();
    }
}