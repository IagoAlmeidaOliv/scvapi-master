package com.example.scvapi.api.controller;

import com.example.scvapi.api.dto.AnimalDTO;
import com.example.scvapi.api.dto.ConsultaDTO;
import com.example.scvapi.exception.RegraNegocioException;
import com.example.scvapi.model.entity.Animal;
import com.example.scvapi.model.entity.Consulta;
import com.example.scvapi.model.entity.Raca;
import com.example.scvapi.model.entity.Tutor;
import com.example.scvapi.service.AnimalService;
import com.example.scvapi.service.RacaService;
import com.example.scvapi.service.TutorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/animais")
@RequiredArgsConstructor
@Api("API de Animais")
public class AnimalController {

    private final AnimalService service;
    private final TutorService tutorService;
    private final RacaService racaService;

    @GetMapping()
    @ApiOperation("Obter todos os animais cadastrados")
    public ResponseEntity<List<AnimalDTO>> get() {
        List<Animal> animais = service.getAnimais();
        return ResponseEntity.ok(animais.stream().map(AnimalDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um Animal")
    public ResponseEntity<Object> get(@PathVariable("id") Long id) {
        return service.getAnimalById(id)
                .map(animal -> ResponseEntity.ok(AnimalDTO.create(animal)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Animal não encontrado"));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<AnimalDTO>> buscarAnimaisPorNome(@RequestParam("nome") String nome) {
        List<Animal> animais = service.buscarPorNome(nome);
        return ResponseEntity.ok(animais.stream().map(AnimalDTO::create).collect(Collectors.toList()));
    }

    @PostMapping()
    @ApiOperation("Adiciona animal à base de dados")
    public ResponseEntity<Object> post(@RequestBody AnimalDTO dto) {
        try {
            Animal animal = converter(dto);
            Animal animalSalvo = service.salvar(animal);
            return new ResponseEntity<>(animalSalvo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Altera detalhes de um animal")
    public ResponseEntity<Object> atualizar(@PathVariable("id") Long id, @RequestBody AnimalDTO dto) {
        if (service.getAnimalById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Animal não encontrado");
        }
        try {
            Animal animal = converter(dto);
            animal.setId(id);
            service.salvar(animal);
            return ResponseEntity.ok(animal);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Exclui um animal do banco de dados")
    public ResponseEntity<Object> excluir(@PathVariable("id") Long id) {
        return service.getAnimalById(id)
                .map(animal -> {
                    service.excluir(animal);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Animal não encontrado"));
    }

    public Animal converter(AnimalDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Animal animal = modelMapper.map(dto, Animal.class);

        if (dto.getIdTutor() != null) {
            tutorService.getTutorById(dto.getIdTutor()).ifPresent(animal::setTutor);
        }
        if (dto.getIdRaca() != null) {
            racaService.getRacaById(dto.getIdRaca()).ifPresent(animal::setRaca);
        }
        return animal;
    }

    @GetMapping("/{id}/consultas")
    @ApiOperation("Obter detalhes de consultas de um animal")
    public ResponseEntity<Object> getConsultas(@PathVariable("id") Long id) {
        return service.getAnimalById(id)
                .map(animal -> ResponseEntity.ok(animal.getConsultas().stream().map(ConsultaDTO::create).collect(Collectors.toList())))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Animal não encontrado"));
    }
}