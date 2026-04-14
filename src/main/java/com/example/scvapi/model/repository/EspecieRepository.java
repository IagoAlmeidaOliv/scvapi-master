package com.example.scvapi.model.repository;

import com.example.scvapi.model.entity.Especie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EspecieRepository extends JpaRepository<Especie, Long> {
}
