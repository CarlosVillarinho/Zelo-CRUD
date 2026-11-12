package com.github.carlosvillarinho.mscidadao.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_cidadao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cidadao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusCidadao statusCidadao;
}
