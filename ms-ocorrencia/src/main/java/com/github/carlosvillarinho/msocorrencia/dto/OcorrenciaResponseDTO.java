package com.github.carlosvillarinho.msocorrencia.dto;

import com.github.carlosvillarinho.msocorrencia.entity.Ocorrencia;
import com.github.carlosvillarinho.msocorrencia.entity.StatusOcorrencia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OcorrenciaResponseDTO {
    private Long id;
    private Long cidadaoId;
    private String titulo;
    private String descricao;
    private String endereco;
    private String categoria;
    private StatusOcorrencia statusOcorrencia;

    public static OcorrenciaResponseDTO fromEntity(Ocorrencia ocorrencia) {
        return new OcorrenciaResponseDTO(
                ocorrencia.getId(),
                ocorrencia.getCidadaoId(),
                ocorrencia.getTitulo(),
                ocorrencia.getDescricao(),
                ocorrencia.getEndereco(),
                ocorrencia.getCategoria(),
                ocorrencia.getStatusOcorrencia()
        );
    }
}
