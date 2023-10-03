package com.rh.testerh.model;

import com.rh.testerh.service.TarefasService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tarefas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tarefas {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "id",nullable = false)
        public UUID id;
        @Column(name = "name",unique = true,nullable = false)
        public String nome;
        @Column(name = "custo",nullable = false)
        public BigDecimal custo;
        @Column(name = "dtLimite",nullable = false)
        public LocalDate dtLimite;
        @Column(name = "ordem",nullable = false)
        public Integer ordemApresentacao;

}
