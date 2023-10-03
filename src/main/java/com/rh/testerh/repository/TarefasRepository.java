package com.rh.testerh.repository;

import com.rh.testerh.model.Tarefas;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Repository
public interface TarefasRepository extends JpaRepository<Tarefas, UUID> {
    @Modifying
    @Transactional
    @Query("UPDATE Tarefas t SET t.nome = :nome, t.dtLimite = :dtLimite, t.custo = :custo WHERE t.id = :id")
    void updateTarefa(UUID id, String nome, LocalDate dtLimite, BigDecimal custo);

    boolean existsByNome(String nome);
    List<Tarefas> findAllByOrderByOrdemApresentacao();

    @Query("SELECT MAX(t.ordemApresentacao) + 1 FROM Tarefas t")
    Integer getNextSequentialValue();

    @Modifying
    @Transactional
    @Query("UPDATE Tarefas t SET t.ordemApresentacao = :novaOrdem WHERE t.id = :id")
    void updateOrdemApresentacao(@Param("id") UUID id, @Param("novaOrdem") int novaOrdem);

}
