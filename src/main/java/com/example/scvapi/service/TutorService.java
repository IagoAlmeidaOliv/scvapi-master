package com.example.scvapi.service;

import com.example.scvapi.exception.RegraNegocioException;
import com.example.scvapi.model.entity.*;
import com.example.scvapi.model.repository.TutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TutorService {

    private TutorRepository repository;

    public TutorService(TutorRepository repository) {
        this.repository = repository;
    }

    public List<Tutor> getTutores() {
        return repository.findAll();
    }

    public Optional<Tutor> getTutorById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Tutor salvar(Tutor tutor) {
        validar(tutor);
        return repository.save(tutor);
    }

    @Transactional
    public void excluir(Tutor tutor) {
        Objects.requireNonNull(tutor.getId());
        repository.delete(tutor);
    }

    public void validar(Tutor tutor) {
        if (tutor.getNome() == null || tutor.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }
        if (tutor.getCpf() == null || tutor.getCpf().trim().equals("")) {
            throw new RegraNegocioException("CPF inválido");
        }
    }
}