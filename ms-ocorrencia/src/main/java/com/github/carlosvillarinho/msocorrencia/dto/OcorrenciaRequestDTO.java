package com.github.carlosvillarinho.msocorrencia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OcorrenciaRequestDTO {
    @NotNull(message = "O ID do cidadão é obrigatório")
    private Long cidadaoId;

    @NotBlank(message = "O título é obrigatório")
    @Size(max = 150, message = "O título deve ter no máximo 150 caracteres")
    private String titulo;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotBlank(message = "O endereço é obrigatório")
    @Size(max = 100, message = "O endereço deve ter no máximo 100 caracteres")
    private String endereco;

    @NotBlank(message = "A categoria é obrigatória")
    @Size(max = 50, message = "A categoria deve ter no máximo 50 caracteres")
    private String categoria;
}
