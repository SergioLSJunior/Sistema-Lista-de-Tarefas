package com.rh.testerh.service;

import com.rh.testerh.model.Tarefas;
import com.rh.testerh.repository.TarefasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TarefasService {
    private final TarefasRepository tarefasRepository;

    @Autowired
    public TarefasService(TarefasRepository tarefasRepository) {
        this.tarefasRepository = tarefasRepository;
    }

    public void updateTarefa(UUID id, String nome, LocalDate dtLimite, BigDecimal custo) {
        tarefasRepository.updateTarefa(id, nome, dtLimite, custo);
    }

    public boolean existsByNome(String nome) {
        return tarefasRepository.existsByNome(nome);
    }

    public List<Tarefas> findAllByOrderByOrdemApresentacao() {
        return tarefasRepository.findAllByOrderByOrdemApresentacao();
    }

    public void deleteById(UUID id) {
        tarefasRepository.deleteById(id);
    }

    public void save(Tarefas tarefas) {
        if (getNextSequentialValue() == null) {
            tarefas.setOrdemApresentacao(1);
        } else {
            tarefas.setOrdemApresentacao(getNextSequentialValue());
        }
        tarefasRepository.saveAndFlush(tarefas);
    }

    public Integer getNextSequentialValue() {
        return tarefasRepository.getNextSequentialValue();
    }

    public void updateOrdem(UUID id, int ordem) {
        tarefasRepository.updateOrdemApresentacao(id, ordem);
    }

}