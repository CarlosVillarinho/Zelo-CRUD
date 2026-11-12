package com.github.carlosvillarinho.mscidadao.service;

import com.github.carlosvillarinho.mscidadao.dto.CidadaoRequestDTO;
import com.github.carlosvillarinho.mscidadao.dto.CidadaoResponseDTO;
import com.github.carlosvillarinho.mscidadao.dto.CidadaoStatusDTO;
import com.github.carlosvillarinho.mscidadao.entity.Cidadao;
import com.github.carlosvillarinho.mscidadao.entity.StatusCidadao;
import com.github.carlosvillarinho.mscidadao.exceptions.DatabaseException;
import com.github.carlosvillarinho.mscidadao.exceptions.ResourceNotFoundException;
import com.github.carlosvillarinho.mscidadao.repository.CidadaoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CidadaoService {
    private final CidadaoRepository repository;

    public CidadaoService(CidadaoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CidadaoResponseDTO> listarTodos() {
        return repository.findAll().stream()
                .map(CidadaoResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public CidadaoResponseDTO buscarPorId(Long id) {
        Cidadao cidadao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cidadão não encontrado com o ID: " + id));
        return CidadaoResponseDTO.fromEntity(cidadao);
    }

    @Transactional
    public CidadaoResponseDTO salvar(CidadaoRequestDTO dto) {
        Cidadao cidadao = new Cidadao();
        cidadao.setNome(dto.getNome());
        cidadao.setCpf(dto.getCpf());
        cidadao.setEmail(dto.getEmail());
        cidadao.setTelefone(dto.getTelefone());
        cidadao.setStatusCidadao(StatusCidadao.ATIVO);

        Cidadao salvo = repository.save(cidadao);
        return CidadaoResponseDTO.fromEntity(salvo);
    }

    @Transactional
    public CidadaoResponseDTO atualizar(Long id, CidadaoRequestDTO dto) {
        Cidadao cidadao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cidadão não encontrado com o ID: " + id));

        cidadao.setNome(dto.getNome());
        cidadao.setCpf(dto.getCpf());
        cidadao.setEmail(dto.getEmail());
        cidadao.setTelefone(dto.getTelefone());

        Cidadao atualizado = repository.save(cidadao);
        return CidadaoResponseDTO.fromEntity(atualizado);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Cidadão não encontrado com o ID: " + id);
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial ao excluir o cidadão.");
        }
    }

    @Transactional
    public CidadaoResponseDTO atualizarStatus(Long id, CidadaoStatusDTO statusDTO) {
        Cidadao cidadao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cidadão não encontrado com o ID: " + id));

        cidadao.setStatusCidadao(statusDTO.getStatusCidadao());
        Cidadao atualizado = repository.save(cidadao);
        return CidadaoResponseDTO.fromEntity(atualizado);
    }
}
