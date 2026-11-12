package com.github.carlosvillarinho.msocorrencia.client;

import com.github.carlosvillarinho.msocorrencia.dto.CidadaoFeignStatusDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ms-cidadao", fallback = CidadaoFeignClientFallback.class)
public interface CidadaoFeignClient {

    @RequestMapping(method = RequestMethod.PUT, value = "/api/v1/cidadaos/{id}/status")
    ResponseEntity<Object> atualizarStatus(
            @PathVariable("id") Long id,
            @RequestBody CidadaoFeignStatusDTO statusDTO
    );
}
