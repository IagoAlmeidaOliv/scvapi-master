package com.example.scvapi.service;

import com.example.scvapi.exception.RegraNegocioException;
import com.example.scvapi.model.entity.Animal;
import com.example.scvapi.model.repository.AnimalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AnimalService {

    private AnimalRepository repository;

    public AnimalService(AnimalRepository repository) {
        this.repository = repository;
    }

    public List<Animal> getAnimais() {
        return repository.findAll();
    }

    public Optional<Animal> getAnimalById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Animal salvar(Animal animal) {
        validar(animal);
        return repository.save(animal);
    }

    @Transactional
    public void excluir(Animal animal) {
        Objects.requireNonNull(animal.getId());
        repository.delete(animal);
    }

    public void validar(Animal animal) {
        if (animal.getNome() == null || animal.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }
        if (animal.getTutor() == null || animal.getTutor().getId() == null) {
            throw new RegraNegocioException("Tutor inválido");
        }
        if (animal.getRaca() == null || animal.getRaca().getId() == null) {
            throw new RegraNegocioException("Raça inválida");
        }
    }
}