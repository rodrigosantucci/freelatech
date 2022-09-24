package com.br.freelatech.repositories;

import com.br.freelatech.models.Trabalho;
import com.br.freelatech.models.Mensagem;
import com.br.freelatech.models.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("mensagemRepository")
public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
    List<Mensagem> findByTrabalho(Trabalho trabalho);

    @Query("SELECT m"
            + " FROM Mensagem m"
            + " WHERE m.trabalho = :trabalho AND ( m.remetente = :contratante OR m.destinatario = :contratante )"
            + " ORDER BY m.id DESC")
    Page<Mensagem> findByTrabalhoAndRemetenteOuDestinatario(
            @Param("trabalho") Trabalho trabalho,
            @Param("contratante") Usuario contratante,
            Pageable request);

    @Query("SELECT m"
            + " FROM Mensagem m"
            + " WHERE m.remetente = :usuario OR m.destinatario = :usuario "
            + " ORDER BY m.id DESC")
    List<Mensagem> findByRemetenteOuDestinatario(@Param("usuario") Usuario eu);

    @Query("SELECT m"
            + " FROM Mensagem m"
            + " WHERE "
            + "( ( m.remetente = :eu AND m.destinatario = :interador ) "
            + " OR "
            + " ( m.remetente = :interador AND m.destinatario = :eu ) ) "
            + " AND m.trabalho is null "
            + " ORDER BY m.id DESC")
    List<Mensagem> findByMeuInterador(@Param("eu") Usuario eu, @Param("interador") Usuario interador);

}