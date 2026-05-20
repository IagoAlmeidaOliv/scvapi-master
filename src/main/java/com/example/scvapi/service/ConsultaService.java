package com.example.scvapi.service;

import com.example.scvapi.exception.RegraNegocioException;
import com.example.scvapi.model.entity.*;
import com.example.scvapi.model.repository.ConsultaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ConsultaService {

    private ConsultaRepository repository;

    public ConsultaService(ConsultaRepository repository) {
        this.repository = repository;
    }

    public List<Consulta> getConsultas() {
        return repository.findAll();
    }

    public Optional<Consulta> getConsultaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Consulta salvar(Consulta consulta) {
        validar(consulta);
        return repository.save(consulta);
    }

    @Transactional
    public void excluir(Consulta consulta) {
        Objects.requireNonNull(consulta.getId());
        repository.delete(consulta);
    }

    public void validar(Consulta consulta) {
        if (consulta.getDataConsulta() == null) {
            throw new RegraNegocioException("Data da consulta inválida");
        }
        if (consulta.getAnimal() == null || consulta.getAnimal().getId() == null) {
            System.out.println(consulta.getAnimal());
            throw new RegraNegocioException("Animal inválido");
        }
        if (consulta.getVeterinario() == null || consulta.getVeterinario().getId() == null) {
            throw new RegraNegocioException("Veterinário inválido");
        }
    }
}