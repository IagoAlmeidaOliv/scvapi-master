package com.example.scvapi.service;

import com.example.scvapi.model.entity.Consulta;
import com.example.scvapi.model.repository.ConsultaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
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
}