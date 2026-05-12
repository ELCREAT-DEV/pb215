package com.pb215.erp.model.Academico;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "aluno_eclesiastica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoEclesiasticaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "igreja_congregacao", nullable = false, length = 100)
    private String igrejaCongregacao;

    @Column(name = "pastor", length = 100)
    private String pastor;

    @Column(name = "membro_desde")
    private LocalDate membroDesde;

    @Column(name = "ministerio_pertencente")
    private Boolean ministerioPertencente;

    @Column(name = "ministerio_qual", length = 150)
    private String ministerioQual;

    @Column(name = "motivo_curso", length = 500)
    private String motivoCurso;

    @Column(name = "pode_participar_sabado")
    private Boolean podeParticiparSabado;

    @Column(name = "motivo_participacao", length = 500)
    private String motivoParticipacao;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private AlunoModel aluno;
}
