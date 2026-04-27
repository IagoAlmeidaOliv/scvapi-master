package com.example.scvapi.api.controller;

import com.example.scvapi.api.dto.AnimalDTO;
import com.example.scvapi.model.entity.Animal;
import com.example.scvapi.service.AnimalService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/animais")
public class AnimalController {

    private final AnimalService service;

    public AnimalController(AnimalService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AnimalDTO>> getAnimais() {
        List<Animal> animais = service.getAnimais();

        List<AnimalDTO> dtos = animais.stream()
                .map(AnimalDTO::create)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalDTO> getAnimalById(@PathVariable Long id) {
        Optional<Animal> animalOpt = service.getAnimalById(id);

        if (animalOpt.isPresent()) {
            AnimalDTO dto = AnimalDTO.create(animalOpt.get());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<AnimalDTO> salvar(@RequestBody AnimalDTO dto) {

        ModelMapper modelMapper = new ModelMapper();
        Animal animal = modelMapper.map(dto, Animal.class);

        Animal animalSalvo = service.salvar(animal);

        return new ResponseEntity<>(AnimalDTO.create(animalSalvo), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        Optional<Animal> animalOpt = service.getAnimalById(id);

        if (animalOpt.isPresent()) {
            service.excluir(animalOpt.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}