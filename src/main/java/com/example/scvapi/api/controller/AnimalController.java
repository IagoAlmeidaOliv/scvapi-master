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
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api("API de Animais")
@CrossOrigin
public class AnimalController {
    private final AnimalService service;
    private final TutorService tutorService;
    private final RacaService racaService;

    @GetMapping()
    @ApiOperation("Obter todos os animais cadastrados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Busca realizada com sucesso"),
            @ApiResponse(code = 404, message = "Erro ao fazer busca")
    })
    public ResponseEntity get() {
        List<Animal> animais = service.getAnimais();
        return ResponseEntity.ok(animais.stream().map(AnimalDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um Animal")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Animal encontrado"),
            @ApiResponse(code = 404, message = "Animal não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Animal> animal = service.getAnimalById(id);
        if (!animal.isPresent()) {
            return new ResponseEntity("Animal não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(animal.map(AnimalDTO::create));
    }

    @GetMapping("/buscar")
    public ResponseEntity buscarAnimaisPorNome(@RequestParam("nome") String nome) {
        List<Animal> animais = service.buscarPorNome(nome);
        return ResponseEntity.ok(animais.stream().map(AnimalDTO::create).collect(Collectors.toList()));
    }

    @PostMapping()
    @ApiOperation("Adiciona animal a base de dados")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Animal adicionado com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar o animal")
    })
    public ResponseEntity post(@RequestBody AnimalDTO dto) {
        try {
            Animal animal = converter(dto);
            animal = service.salvar(animal);
            return new ResponseEntity(animal, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Altera detalhes de um animal")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Dados alterados com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao alterar dados do animal"),
            @ApiResponse(code = 404, message = "Animal não encontrado")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody AnimalDTO dto) {
        if (!service.getAnimalById(id).isPresent()) {
            return new ResponseEntity("Animal não encontrado", HttpStatus.NOT_FOUND);
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
    @ApiResponses({
            @ApiResponse(code = 200, message = "Animal excluido com sucesso"),
            @ApiResponse(code = 404, message = "Animal não encontrado")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Animal> animal = service.getAnimalById(id);
        if (!animal.isPresent()) {
            return new ResponseEntity("Animal não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(animal.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Animal converter(AnimalDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Animal animal = modelMapper.map(dto, Animal.class);

        if (dto.getIdTutor() != null) {
            Optional<Tutor> tutor = tutorService.getTutorById(dto.getIdTutor());
            if (!tutor.isPresent()) {
                animal.setTutor(null);
            } else {
                animal.setTutor(tutor.get());
            }
        }

        if (dto.getIdRaca() != null) {
            Optional<Raca> raca = racaService.getRacaById(dto.getIdRaca());
            if (!raca.isPresent()) {
                animal.setRaca(null);
            } else {
                animal.setRaca(raca.get());
            }
        }

        return animal;
    }

    @GetMapping("/{id}/consultas")
    @ApiOperation("Obter detalhes de consultas de um animal")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Animal encontrado"),
            @ApiResponse(code = 404, message = "Animal não encontrado")
    })
    public ResponseEntity getConsultas(@PathVariable("id") Long id) {
        Optional<Animal> animal = service.getAnimalById(id);
        if (!animal.isPresent()) {
            return new ResponseEntity("Animal não encontrado", HttpStatus.NOT_FOUND);
        }
        List<Consulta> consultas = animal.get().getConsultas();
        return ResponseEntity.ok(consultas.stream().map(ConsultaDTO::create).collect(Collectors.toList()));
    }
}