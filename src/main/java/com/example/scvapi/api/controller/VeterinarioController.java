package com.example.scvapi.api.controller;

import com.example.scvapi.api.dto.VeterinarioDTO;
import com.example.scvapi.api.dto.ConsultaDTO;
import com.example.scvapi.exception.RegraNegocioException;
import com.example.scvapi.model.entity.Consulta;
import com.example.scvapi.model.entity.Veterinario;
import com.example.scvapi.service.VeterinarioService;
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
@RequestMapping("/api/v1/veterinarios")
@RequiredArgsConstructor
@Api("API de Veterinários")
@CrossOrigin
public class VeterinarioController {

    private final VeterinarioService service;

    @GetMapping()
    @ApiOperation("Obter todos os veterinários cadastrados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Busca realizada com sucesso"),
            @ApiResponse(code = 404, message = "Erro ao fazer busca")
    })
    public ResponseEntity get() {
        List<Veterinario> veterinarios = service.getVeterinarios();
        return ResponseEntity.ok(veterinarios.stream().map(VeterinarioDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um Veterinário")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Veterinário encontrado"),
            @ApiResponse(code = 404, message = "Veterinário não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Veterinario> veterinario = service.getVeterinarioById(id);
        if (!veterinario.isPresent()) {
            return new ResponseEntity("Veterinário não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(veterinario.map(VeterinarioDTO::create));
    }

    @PostMapping()
    @ApiOperation("Adiciona veterinário a base de dados")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Veterinário adicionado com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar o veterinário")
    })
    public ResponseEntity post(@RequestBody VeterinarioDTO dto) {
        try {
            Veterinario veterinario = converter(dto);
            veterinario = service.salvar(veterinario);
            return new ResponseEntity(veterinario, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Altera detalhes de um veterinário")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Dados alterados com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao alterar dados do veterinário"),
            @ApiResponse(code = 404, message = "Veterinário não encontrado")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody VeterinarioDTO dto) {
        if (!service.getVeterinarioById(id).isPresent()) {
            return new ResponseEntity("Veterinário não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Veterinario veterinario = converter(dto);
            veterinario.setId(id);
            service.salvar(veterinario);
            return ResponseEntity.ok(veterinario);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Exclui um veterinário do banco de dados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Veterinário excluido com sucesso"),
            @ApiResponse(code = 404, message = "Veterinário não encontrado")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Veterinario> veterinario = service.getVeterinarioById(id);
        if (!veterinario.isPresent()) {
            return new ResponseEntity("Veterinário não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(veterinario.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Veterinario converter(VeterinarioDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Veterinario.class);
    }

    @GetMapping("/{id}/consultas")
    @ApiOperation("Obter detalhes de consultas de um veterinário")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Veterinário encontrado"),
            @ApiResponse(code = 404, message = "Veterinário não encontrado")
    })
    public ResponseEntity getConsultas(@PathVariable("id") Long id) {
        Optional<Veterinario> veterinario = service.getVeterinarioById(id);
        if (!veterinario.isPresent()) {
            return new ResponseEntity("Veterinário não encontrado", HttpStatus.NOT_FOUND);
        }
        List<Consulta> consultas = veterinario.get().getConsultas();
        return ResponseEntity.ok(consultas.stream().map(ConsultaDTO::create).collect(Collectors.toList()));
    }
}