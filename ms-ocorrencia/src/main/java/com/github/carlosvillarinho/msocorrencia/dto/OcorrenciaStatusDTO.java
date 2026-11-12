package com.github.carlosvillarinho.msocorrencia.dto;

import com.github.carlosvillarinho.msocorrencia.entity.StatusOcorrencia;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OcorrenciaStatusDTO {
    @NotNull(message = "O status da ocorrência é obrigatório")
    private StatusOcorrencia statusOcorrencia;
}