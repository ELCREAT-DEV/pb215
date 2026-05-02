package com.pb215.erp.model.Academico;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "turma")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurmaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String nome;

    @Column(name = "codigo_turma", nullable = false, unique = true)
    private String codigoTurma;

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private CursoModel curso;

    @Column(name = "turno")
    private String turno;

    @Column(name = "capacidade")
    private Integer capacidade;

    @Column(name = "turma_fechada")
    private Boolean turmaFechada;

    @Column(name = "status")
    private String status;
    
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}
