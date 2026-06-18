package com.example.scvapi.api.controller;

import com.example.scvapi.api.dto.ProcedimentoDTO;
import com.example.scvapi.exception.RegraNegocioException;
import com.example.scvapi.model.entity.Procedimento;
import com.example.scvapi.service.ProcedimentoService;
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
@RequestMapping("/api/v1/procedimentos")
@RequiredArgsConstructor
@Api("API de Procedimentos")
@CrossOrigin
public class ProcedimentoController {

    private final ProcedimentoService service;

    @GetMapping()
    @ApiOperation("Obter todos os procedimentos cadastrados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Busca realizada com sucesso"),
            @ApiResponse(code = 404, message = "Erro ao fazer busca")
    })
    public ResponseEntity get() {
        List<Procedimento> procedimentos = service.getProcedimentos();
        return ResponseEntity.ok(procedimentos.stream().map(ProcedimentoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um Procedimento")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Procedimento encontrado"),
            @ApiResponse(code = 404, message = "Procedimento não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Procedimento> procedimento = service.getProcedimentoById(id);
        if (!procedimento.isPresent()) {
            return new ResponseEntity("Procedimento não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(procedimento.map(ProcedimentoDTO::create));
    }

    @PostMapping()
    @ApiOperation("Adiciona procedimento a base de dados")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Procedimento adicionado com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar o procedimento")
    })
    public ResponseEntity post(@RequestBody ProcedimentoDTO dto) {
        try {
            Procedimento procedimento = converter(dto);
            procedimento = service.salvar(procedimento);
            return new ResponseEntity(procedimento, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Altera detalhes de um procedimento")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Dados alterados com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao alterar dados do procedimento"),
            @ApiResponse(code = 404, message = "Procedimento não encontrado")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ProcedimentoDTO dto) {
        if (!service.getProcedimentoById(id).isPresent()) {
            return new ResponseEntity("Procedimento não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Procedimento procedimento = converter(dto);
            procedimento.setId(id);
            service.salvar(procedimento);
            return ResponseEntity.ok(procedimento);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Exclui um procedimento do banco de dados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Procedimento excluido com sucesso"),
            @ApiResponse(code = 404, message = "Procedimento não encontrado")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Procedimento> procedimento = service.getProcedimentoById(id);
        if (!procedimento.isPresent()) {
            return new ResponseEntity("Procedimento não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(procedimento.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Procedimento converter(ProcedimentoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Procedimento.class);
    }
}