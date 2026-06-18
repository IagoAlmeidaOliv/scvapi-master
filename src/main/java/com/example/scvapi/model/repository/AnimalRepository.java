package com.example.scvapi.model.repository;

import com.example.scvapi.model.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
    List<Animal> findByNomeContainingIgnoreCase(String nome);
}