package com.github.carlosvillarinho.mscidadao.controller;

import com.github.carlosvillarinho.mscidadao.dto.CidadaoRequestDTO;
import com.github.carlosvillarinho.mscidadao.dto.CidadaoResponseDTO;
import com.github.carlosvillarinho.mscidadao.dto.CidadaoStatusDTO;
import com.github.carlosvillarinho.mscidadao.service.CidadaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cidadaos")
public class CidadaoController {
    private final CidadaoService service;

    public CidadaoController(CidadaoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CidadaoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CidadaoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CidadaoResponseDTO> salvar(@Valid @RequestBody CidadaoRequestDTO dto) {
        CidadaoResponseDTO novoCidadao = service.salvar(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoCidadao.getId())
                .toUri();
        return ResponseEntity.created(uri).body(novoCidadao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CidadaoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody CidadaoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CidadaoResponseDTO> atualizarStatus(@PathVariable Long id, @Valid @RequestBody CidadaoStatusDTO statusDTO) {
        return ResponseEntity.ok(service.atualizarStatus(id, statusDTO));
    }
}
