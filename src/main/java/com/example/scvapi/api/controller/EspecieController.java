package com.example.scvapi.api.controller;

import com.example.scvapi.api.dto.EspecieDTO;
import com.example.scvapi.api.dto.RacaDTO;
import com.example.scvapi.exception.RegraNegocioException;
import com.example.scvapi.model.entity.Especie;
import com.example.scvapi.model.entity.Raca;
import com.example.scvapi.service.EspecieService;
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
@RequestMapping("/api/v1/especies")
@RequiredArgsConstructor
@Api("API de Espécies")
@CrossOrigin
public class EspecieController {

    private final EspecieService service;

    @GetMapping()
    @ApiOperation("Obter todas as espécies cadastradas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Busca realizada com sucesso"),
            @ApiResponse(code = 404, message = "Erro ao fazer busca")
    })
    public ResponseEntity get() {
        List<Especie> especies = service.getEspecies();
        return ResponseEntity.ok(especies.stream().map(EspecieDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de uma Espécie")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Espécie encontrada"),
            @ApiResponse(code = 404, message = "Espécie não encontrada")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Especie> especie = service.getEspecieById(id);
        if (!especie.isPresent()) {
            return new ResponseEntity("Espécie não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(especie.map(EspecieDTO::create));
    }

    @PostMapping()
    @ApiOperation("Adiciona espécie a base de dados")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Espécie adicionada com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar a espécie")
    })
    public ResponseEntity post(@RequestBody EspecieDTO dto) {
        try {
            Especie especie = converter(dto);
            especie = service.salvar(especie);
            return new ResponseEntity(especie, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Altera detalhes de uma espécie")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Dados alterados com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao alterar dados da espécie"),
            @ApiResponse(code = 404, message = "Espécie não encontrada")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody EspecieDTO dto) {
        if (!service.getEspecieById(id).isPresent()) {
            return new ResponseEntity("Espécie não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Especie especie = converter(dto);
            especie.setId(id);
            service.salvar(especie);
            return ResponseEntity.ok(especie);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Exclui uma espécie do banco de dados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Espécie excluida com sucesso"),
            @ApiResponse(code = 404, message = "Espécie não encontrada")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Especie> especie = service.getEspecieById(id);
        if (!especie.isPresent()) {
            return new ResponseEntity("Especie não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(especie.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Especie converter(EspecieDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Especie.class);
    }

    @GetMapping("/{id}/racas")
    @ApiOperation("Obter detalhes de raça de uma espécie")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Espécie encontrada"),
            @ApiResponse(code = 404, message = "Espécie não encontrada")
    })
    public ResponseEntity getRacas(@PathVariable("id") Long id) {
        Optional<Especie> especie = service.getEspecieById(id);
        if (!especie.isPresent()) {
            return new ResponseEntity("Especie não encontrada", HttpStatus.NOT_FOUND);
        }
        List<Raca> racas = especie.get().getRacas();
        return ResponseEntity.ok(racas.stream().map(RacaDTO::create).collect(Collectors.toList()));
    }
}