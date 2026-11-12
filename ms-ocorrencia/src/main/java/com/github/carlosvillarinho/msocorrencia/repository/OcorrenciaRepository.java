package com.github.carlosvillarinho.msocorrencia.repository;

import com.github.carlosvillarinho.msocorrencia.entity.Ocorrencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, Long> {
}
