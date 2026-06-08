package com.example.scvapi.api.controller;

import com.example.scvapi.api.dto.RacaDTO;
import com.example.scvapi.exception.RegraNegocioException;
import com.example.scvapi.model.entity.Especie;
import com.example.scvapi.model.entity.Raca;
import com.example.scvapi.service.EspecieService;
import com.example.scvapi.service.RacaService;
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
@RequestMapping("/api/v1/racas")
@RequiredArgsConstructor
@Api("API de Raças")
@CrossOrigin
public class RacaController {

    private final RacaService service;
    private final EspecieService especieService;

    @GetMapping()
    @ApiOperation("Obter todas as raças cadastradas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Busca realizada com sucesso"),
            @ApiResponse(code = 404, message = "Erro ao fazer busca")
    })
    public ResponseEntity get() {
        List<Raca> racas = service.getRacas();
        return ResponseEntity.ok(racas.stream().map(RacaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de uma Raça")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Raça encontrada"),
            @ApiResponse(code = 404, message = "Raça não encontrada")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Raca> raca = service.getRacaById(id);
        if (!raca.isPresent()) {
            return new ResponseEntity("Raça não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(raca.map(RacaDTO::create));
    }

    @PostMapping()
    @ApiOperation("Adiciona raça a base de dados")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Raça adicionada com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar a raça")
    })
    public ResponseEntity post(@RequestBody RacaDTO dto) {
        try {
            Raca raca = converter(dto);
            raca = service.salvar(raca);
            return new ResponseEntity(raca, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Altera detalhes de uma raça")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Dados alterados com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao alterar dados da raça"),
            @ApiResponse(code = 404, message = "Raça não encontrada")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody RacaDTO dto) {
        if (!service.getRacaById(id).isPresent()) {
            return new ResponseEntity("Raça não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Raca raca = converter(dto);
            raca.setId(id);
            service.salvar(raca);
            return ResponseEntity.ok(raca);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Exclui uma raça do banco de dados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Raça excluida com sucesso"),
            @ApiResponse(code = 404, message = "Raça não encontrada")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Raca> raca = service.getRacaById(id);
        if (!raca.isPresent()) {
            return new ResponseEntity("Raça não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(raca.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    public Raca converter(RacaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Raca raca = modelMapper.map(dto, Raca.class);

        if (dto.getIdEspecie() != null) {
            Optional<Especie> especie = especieService.getEspecieById(dto.getIdEspecie());
            if (!especie.isPresent()) {
                raca.setEspecie(null);
            } else {
                raca.setEspecie(especie.get());
            }
        }
        return raca;
    }
}