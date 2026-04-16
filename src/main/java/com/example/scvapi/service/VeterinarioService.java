package com.example.scvapi.service;

import com.example.scvapi.exception.RegraNegocioException;
import com.example.scvapi.model.entity.Veterinario;
import com.example.scvapi.model.repository.VeterinarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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

    @Transactional
    public Veterinario salvar(Veterinario veterinario) {
        validar(veterinario);
        return repository.save(veterinario);
    }

    @Transactional
    public void excluir(Veterinario veterinario) {
        Objects.requireNonNull(veterinario.getId());
        repository.delete(veterinario);
    }

    public void validar(Veterinario veterinario) {
        if (veterinario.getNome() == null || veterinario.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }
        if (veterinario.getCrmv() == null || veterinario.getCrmv().trim().equals("")) {
            throw new RegraNegocioException("CRMV inválido");
        }
    }
}