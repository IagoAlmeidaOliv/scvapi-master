package com.example.scvapi.model.repository;

import com.example.scvapi.model.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
}