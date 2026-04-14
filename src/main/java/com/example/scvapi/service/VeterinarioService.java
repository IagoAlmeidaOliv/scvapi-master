package com.example.scvapi.service;

import com.example.scvapi.model.entity.Veterinario;
import com.example.scvapi.model.repository.VeterinarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class VeterinarioService {

    private VeterinarioRepository repository;

    public VeterinarioService(VeterinarioRepository repository) {
        this.repository = repository;
    }

    public List<Veterinario> getVeterinarios() {
        return repository.findAll();
    }

    public Optional<Veterinario> getVeterinarioById(Long id) {
        return repository.findById(id);
    }
}