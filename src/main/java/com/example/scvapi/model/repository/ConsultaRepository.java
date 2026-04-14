package com.example.scvapi.model.repository;

import com.example.scvapi.model.entity.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
}