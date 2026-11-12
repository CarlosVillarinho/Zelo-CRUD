package com.github.carlosvillarinho.mscidadao.dto;

import com.github.carlosvillarinho.mscidadao.entity.Cidadao;
import com.github.carlosvillarinho.mscidadao.entity.StatusCidadao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CidadaoResponseDTO {
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private StatusCidadao statusCidadao;

    public static CidadaoResponseDTO fromEntity(Cidadao cidadao) {
        return new CidadaoResponseDTO(
                cidadao.getId(),
                cidadao.getNome(),
                cidadao.getCpf(),
                cidadao.getEmail(),
                cidadao.getTelefone(),
                cidadao.getStatusCidadao()
        );
    }
}
