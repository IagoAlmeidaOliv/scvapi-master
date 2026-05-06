package com.example.scvapi.api.controller;

import com.example.scvapi.api.dto.AnimalDTO;
import com.example.scvapi.exception.RegraNegocioException;
import com.example.scvapi.model.entity.Animal;
import com.example.scvapi.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/animais")
@RequiredArgsConstructor
@CrossOrigin
public class AnimalController {
    private final AnimalService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Animal> animais = service.getAnimais();
        return ResponseEntity.ok(animais.stream().map(AnimalDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Animal> animal = service.getAnimalById(id);
        if (!animal.isPresent()) {
            return new ResponseEntity("Animal não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(animal.map(AnimalDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody AnimalDTO dto) {
        try {
            Animal animal = converter(dto);
            animal = service.salvar(animal);
            return new ResponseEntity(animal, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Animal converter(AnimalDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Animal animal = modelMapper.map(dto, Animal.class);
        return animal;
    }
}