package com.example.scvapi.api.controller;

import com.example.scvapi.api.dto.ConsultaDTO;
import com.example.scvapi.api.dto.ProcedimentoDTO;
import com.example.scvapi.exception.RegraNegocioException;
import com.example.scvapi.model.entity.Animal;
import com.example.scvapi.model.entity.Consulta;
import com.example.scvapi.model.entity.Procedimento;
import com.example.scvapi.model.entity.Veterinario;
import com.example.scvapi.service.AnimalService;
import com.example.scvapi.service.ConsultaService;
import com.example.scvapi.service.VeterinarioService;
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
@RequestMapping("/api/v1/consultas")
@RequiredArgsConstructor
@CrossOrigin
public class ConsultaController {

    private final ConsultaService service;
    private final AnimalService animalService;
    private final VeterinarioService veterinarioService;

    @GetMapping()
    @ApiOperation("Obter todas as consultas cadastradas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Busca realizada com sucesso"),
            @ApiResponse(code = 404, message = "Erro ao fazer busca")
    })
    public ResponseEntity get() {
        List<Consulta> consultas = service.getConsultas();
        return ResponseEntity.ok(consultas.stream().map(ConsultaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de uma consulta")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Consulta> consulta = service.getConsultaById(id);
        if (!consulta.isPresent()) {
            return new ResponseEntity("Consulta não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(consulta.map(ConsultaDTO::create));
    }

    @PostMapping()
    @ApiOperation("Adiciona consulta a base de dados")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Consulta adicionada com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar a consulta")
    })
    public ResponseEntity post(@RequestBody ConsultaDTO dto) {
        try {
            Consulta consulta = converter(dto);
            consulta = service.salvar(consulta);
            return new ResponseEntity(consulta, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Altera detalhes de uma consulta")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Dados alterados com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao alterar dados da consulta"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ConsultaDTO dto) {
        if (!service.getConsultaById(id).isPresent()) {
            return new ResponseEntity("Consulta não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Consulta consulta = converter(dto);
            consulta.setId(id);
            service.salvar(consulta);
            return ResponseEntity.ok(consulta);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Exclui uma consulta do banco de dados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta excluida com sucesso"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Consulta> consulta = service.getConsultaById(id);
        if (!consulta.isPresent()) {
            return new ResponseEntity("Consulta não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(consulta.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Consulta converter(ConsultaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Consulta consulta = modelMapper.map(dto, Consulta.class);

        if (dto.getIdAnimal() != null) {
            Optional<Animal> animal = animalService.getAnimalById(dto.getIdAnimal());
            if (!animal.isPresent()) {
                consulta.setAnimal(null);
            } else {
                consulta.setAnimal(animal.get());
            }
        }

        if (dto.getIdVeterinario() != null) {
            Optional<Veterinario> veterinario = veterinarioService.getVeterinarioById(dto.getIdVeterinario());
            if (!veterinario.isPresent()) {
                consulta.setVeterinario(null);
            } else {
                consulta.setVeterinario(veterinario.get());
            }
        }

        return consulta;
    }

    @GetMapping("/{id}/procedimentos")
    @ApiOperation("Obter detalhes de procedimento de uma consulta")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    public ResponseEntity getProcedimentos(@PathVariable("id") Long id) {
        Optional<Consulta> consulta = service.getConsultaById(id);
        if (!consulta.isPresent()) {
            return new ResponseEntity("Consulta não encontrado", HttpStatus.NOT_FOUND);
        }
        List<Procedimento> procedimentos = consulta.get().getProcedimentos();
        return ResponseEntity.ok(procedimentos.stream().map(ProcedimentoDTO::create).collect(Collectors.toList()));
    }
}