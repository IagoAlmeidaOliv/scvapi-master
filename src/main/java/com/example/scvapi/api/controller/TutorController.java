package com.example.scvapi.api.controller;

import com.example.scvapi.api.dto.AnimalDTO;
import com.example.scvapi.api.dto.TutorDTO;
import com.example.scvapi.exception.RegraNegocioException;
import com.example.scvapi.model.entity.Animal;
import com.example.scvapi.model.entity.Tutor;
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
@RequestMapping("/api/v1/tutores")
@RequiredArgsConstructor
@Api("API de Tutores")
@CrossOrigin
public class TutorController {

    private final TutorService service;

    @GetMapping()
    @ApiOperation("Obter todos os tutores cadastrados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Busca realizada com sucesso"),
            @ApiResponse(code = 404, message = "Erro ao fazer busca")
    })
    public ResponseEntity get() {
        List<Tutor> tutores = service.getTutores();
        return ResponseEntity.ok(tutores.stream().map(TutorDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um Tutor")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Tutor encontrado"),
            @ApiResponse(code = 404, message = "Tutor não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Tutor> tutor = service.getTutorById(id);
        if (!tutor.isPresent()) {
            return new ResponseEntity("Tutor não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tutor.map(TutorDTO::create));
    }

    @PostMapping()
    @ApiOperation("Adiciona tutor a base de dados")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Tutor adicionado com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar o tutor")
    })
    public ResponseEntity post(@RequestBody TutorDTO dto) {
        try {
            Tutor tutor = converter(dto);
            tutor = service.salvar(tutor);
            return new ResponseEntity(tutor, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ApiOperation("Altera detalhes de um tutor")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Dados alterados com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao alterar dados do tutor"),
            @ApiResponse(code = 404, message = "Tutor não encontrado")
    })
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

    @DeleteMapping("{id}")
    @ApiOperation("Exclui um tutor do banco de dados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Tutor excluido com sucesso"),
            @ApiResponse(code = 404, message = "Tutor não encontrado")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Tutor> tutor = service.getTutorById(id);
        if (!tutor.isPresent()) {
            return new ResponseEntity("Tutor não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(tutor.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Tutor converter(TutorDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Tutor.class);
    }

    @GetMapping("/{id}/animais")
    @ApiOperation("Obter detalhes de animais de um tutor")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Tutor encontrado"),
            @ApiResponse(code = 404, message = "Tutor não encontrado")
    })
    public ResponseEntity getAnimais(@PathVariable("id") Long id) {
        Optional<Tutor> tutor = service.getTutorById(id);
        if (!tutor.isPresent()) {
            return new ResponseEntity("Tutor não encontrado", HttpStatus.NOT_FOUND);
        }
        List<Animal> animais = tutor.get().getAnimais();
        return ResponseEntity.ok(animais.stream().map(AnimalDTO::create).collect(Collectors.toList()));
    }
}