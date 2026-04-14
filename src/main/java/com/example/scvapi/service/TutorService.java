package com.example.scvapi.service;

import com.example.scvapi.model.entity.Tutor;
import com.example.scvapi.model.repository.TutorRepository;
import org.springframework.stereotype.Service;
import java.util.List;
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
}