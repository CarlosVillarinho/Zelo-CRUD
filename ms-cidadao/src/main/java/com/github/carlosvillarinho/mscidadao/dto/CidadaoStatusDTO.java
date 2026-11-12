package com.github.carlosvillarinho.mscidadao.dto;

import com.github.carlosvillarinho.mscidadao.entity.StatusCidadao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CidadaoStatusDTO {

    @NotNull(message = "O status é obrigatório")
    private StatusCidadao statusCidadao;
}
