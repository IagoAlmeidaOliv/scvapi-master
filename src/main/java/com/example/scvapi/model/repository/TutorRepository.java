package com.example.scvapi.model.repository;

import com.example.scvapi.model.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
    List<Tutor> findByNomeContainingIgnoreCase(String nome);
}