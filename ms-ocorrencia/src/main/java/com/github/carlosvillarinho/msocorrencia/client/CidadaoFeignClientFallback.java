package com.github.carlosvillarinho.msocorrencia.client;

import com.github.carlosvillarinho.msocorrencia.dto.CidadaoFeignStatusDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CidadaoFeignClientFallback implements CidadaoFeignClient {
    @Override
    public ResponseEntity<Object> atualizarStatus(Long id, CidadaoFeignStatusDTO statusDTO) {
        System.err.println("Fallback acionado: Não foi possível atualizar o status do cidadão ID: " + id);
        return ResponseEntity.ok().build();
    }
}