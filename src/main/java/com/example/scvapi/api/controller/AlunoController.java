package com.example.scvapi.api.controller;

import com.example.scvapi.api.dto.AlunoDTO;

import com.example.scvapi.api.dto.AtividadeComplementarDTO;
import com.example.scvapi.api.dto.EstagioDTO;
import com.example.scvapi.exception.RegraNegocioException;
import com.example.scvapi.model.entity.Aluno;
import com.example.scvapi.model.entity.AtividadeComplementar;
import com.example.scvapi.model.entity.Curso;
import com.example.scvapi.model.entity.Estagio;
import com.example.scvapi.service.AlunoService;
import com.example.scvapi.service.AtividadeComplementarService;
import com.example.scvapi.service.CursoService;
import com.example.scvapi.service.EstagioService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/alunos")
@RequiredArgsConstructor
@CrossOrigin
public class AlunoController {

    private final AlunoService service;
    private final CursoService cursoService;
    private final EstagioService estagioService;
    private final AtividadeComplementarService atividadeComplementarService;

    @GetMapping()
    public ResponseEntity get() {
        List<Aluno> alunos = service.getAlunos();
        return ResponseEntity.ok(alunos.stream().map(AlunoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Aluno> aluno = service.getAlunoById(id);
        if (!aluno.isPresent()) {
            return new ResponseEntity("Aluno não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(aluno.map(AlunoDTO::create));
    }

    @GetMapping("{id}/estagios")
    public ResponseEntity getEstagios(@PathVariable("id") Long id) {
        Optional<Aluno> aluno = service.getAlunoById(id);
        if (!aluno.isPresent()) {
            return new ResponseEntity("Aluno não encontrado", HttpStatus.NOT_FOUND);
        }
        List<Estagio> estagios = estagioService.getEstagiosByAluno(aluno);
        return ResponseEntity.ok(estagios.stream().map(EstagioDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("{id}/atividadescomplementares")
    public ResponseEntity getAtividadesComplementares(@PathVariable("id") Long id) {
        Optional<Aluno> aluno = service.getAlunoById(id);
        if (!aluno.isPresent()) {
            return new ResponseEntity("Aluno não encontrado", HttpStatus.NOT_FOUND);
        }
        List<AtividadeComplementar> atividadesComplementares = atividadeComplementarService.getAtividadesComplementaresByAluno(aluno);
        return ResponseEntity.ok(atividadesComplementares.stream().map(AtividadeComplementarDTO::create).collect(Collectors.toList()));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody AlunoDTO dto) {
        try {
            Aluno aluno = converter(dto);
            aluno = service.salvar(aluno);
            return new ResponseEntity(aluno, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody AlunoDTO dto) {
        if (!service.getAlunoById(id).isPresent()) {
            return new ResponseEntity("Aluno não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Aluno aluno = converter(dto);
            aluno.setId(id);
            service.salvar(aluno);
            return ResponseEntity.ok(aluno);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Aluno> aluno = service.getAlunoById(id);
        if (!aluno.isPresent()) {
            return new ResponseEntity("Aluno não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(aluno.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Aluno converter(AlunoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Aluno aluno = modelMapper.map(dto, Aluno.class);
        if (dto.getIdCurso() != null) {
            Optional<Curso> curso = cursoService.getCursoById(dto.getIdCurso());
            if (!curso.isPresent()) {
                aluno.setCurso(null);
            } else {
                aluno.setCurso(curso.get());
            }
        }
        return aluno;
    }
}
