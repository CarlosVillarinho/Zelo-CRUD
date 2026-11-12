package com.github.carlosvillarinho.msocorrencia.service;

import com.github.carlosvillarinho.msocorrencia.client.CidadaoFeignClient;
import com.github.carlosvillarinho.msocorrencia.dto.CidadaoFeignStatusDTO;
import com.github.carlosvillarinho.msocorrencia.dto.OcorrenciaRequestDTO;
import com.github.carlosvillarinho.msocorrencia.dto.OcorrenciaResponseDTO;
import com.github.carlosvillarinho.msocorrencia.dto.OcorrenciaStatusDTO;
import com.github.carlosvillarinho.msocorrencia.entity.Ocorrencia;
import com.github.carlosvillarinho.msocorrencia.entity.StatusOcorrencia;
import com.github.carlosvillarinho.msocorrencia.exceptions.DatabaseException;
import com.github.carlosvillarinho.msocorrencia.exceptions.ResourceNotFoundException;
import com.github.carlosvillarinho.msocorrencia.repository.OcorrenciaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OcorrenciaService {
    private final OcorrenciaRepository repository;
    private final CidadaoFeignClient cidadaoFeignClient;

    public OcorrenciaService(OcorrenciaRepository repository, CidadaoFeignClient cidadaoFeignClient) {
        this.repository = repository;
        this.cidadaoFeignClient = cidadaoFeignClient;
    }

    @Transactional(readOnly = true)
    public List<OcorrenciaResponseDTO> listarTodas() {
        return repository.findAll().stream()
                .map(OcorrenciaResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public OcorrenciaResponseDTO buscarPorId(Long id) {
        Ocorrencia ocorrencia = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ocorrência não encontrada com o ID: " + id));
        return OcorrenciaResponseDTO.fromEntity(ocorrencia);
    }

    @Transactional
    public OcorrenciaResponseDTO salvar(OcorrenciaRequestDTO dto) {
        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setCidadaoId(dto.getCidadaoId());
        ocorrencia.setTitulo(dto.getTitulo());
        ocorrencia.setDescricao(dto.getDescricao());
        ocorrencia.setEndereco(dto.getEndereco());
        ocorrencia.setCategoria(dto.getCategoria());
        ocorrencia.setStatusOcorrencia(StatusOcorrencia.ABERTA);

        Ocorrencia salva = repository.save(ocorrencia);

        cidadaoFeignClient.atualizarStatus(salva.getCidadaoId(), new CidadaoFeignStatusDTO("ATIVO"));

        return OcorrenciaResponseDTO.fromEntity(salva);
    }

    @Transactional
    public OcorrenciaResponseDTO atualizar(Long id, OcorrenciaRequestDTO dto) {
        Ocorrencia ocorrencia = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ocorrência não encontrada com o ID: " + id));

        ocorrencia.setCidadaoId(dto.getCidadaoId());
        ocorrencia.setTitulo(dto.getTitulo());
        ocorrencia.setDescricao(dto.getDescricao());
        ocorrencia.setEndereco(dto.getEndereco());
        ocorrencia.setCategoria(dto.getCategoria());

        Ocorrencia atualizada = repository.save(ocorrencia);
        return OcorrenciaResponseDTO.fromEntity(atualizada);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Ocorrência não encontrada com o ID: " + id);
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial ao excluir a ocorrência.");
        }
    }

    @Transactional
    public OcorrenciaResponseDTO atualizarStatus(Long id, OcorrenciaStatusDTO statusDTO) {
        Ocorrencia ocorrencia = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ocorrência não encontrada com o ID: " + id));

        ocorrencia.setStatusOcorrencia(statusDTO.getStatusOcorrencia());
        Ocorrencia atualizada = repository.save(ocorrencia);
        return OcorrenciaResponseDTO.fromEntity(atualizada);
    }
}
