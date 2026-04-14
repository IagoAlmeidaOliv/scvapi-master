package com.example.scvapi.model.repository;

import com.example.scvapi.model.entity.Procedimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcedimentoRepository extends JpaRepository<Procedimento, Long> {
}
