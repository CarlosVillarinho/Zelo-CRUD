package com.github.carlosvillarinho.msocorrencia.controller;

import com.github.carlosvillarinho.msocorrencia.dto.OcorrenciaRequestDTO;
import com.github.carlosvillarinho.msocorrencia.dto.OcorrenciaResponseDTO;
import com.github.carlosvillarinho.msocorrencia.dto.OcorrenciaStatusDTO;
import com.github.carlosvillarinho.msocorrencia.service.OcorrenciaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ocorrencias")
public class OcorrenciaController {
    private final OcorrenciaService service;

    public OcorrenciaController(OcorrenciaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<OcorrenciaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OcorrenciaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<OcorrenciaResponseDTO> salvar(@Valid @RequestBody OcorrenciaRequestDTO dto) {
        OcorrenciaResponseDTO novaOcorrencia = service.salvar(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novaOcorrencia.getId())
                .toUri();
        return ResponseEntity.created(uri).body(novaOcorrencia);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OcorrenciaResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody OcorrenciaRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OcorrenciaResponseDTO> atualizarStatus(@PathVariable Long id, @Valid @RequestBody OcorrenciaStatusDTO statusDTO) {
        return ResponseEntity.ok(service.atualizarStatus(id, statusDTO));
    }
}
