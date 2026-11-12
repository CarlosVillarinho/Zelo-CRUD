package com.github.carlosvillarinho.mscidadao.repository;

import com.github.carlosvillarinho.mscidadao.entity.Cidadao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CidadaoRepository extends JpaRepository<Cidadao, Long> {
}
