package com.github.carlosvillarinho.msocorrencia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_ocorrencia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ocorrencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long cidadaoId;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, length = 100)
    private String endereco;

    @Column(nullable = false, length = 50)
    private String categoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusOcorrencia statusOcorrencia;
}
