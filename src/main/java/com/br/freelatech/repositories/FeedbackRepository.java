package com.br.freelatech.repositories;

import com.br.freelatech.models.Proposta;
import com.br.freelatech.models.Feedback;
import com.br.freelatech.models.Trabalho;
import com.br.freelatech.models.Usuario;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;




@Repository("tb_feedback")
public interface FeedbackRepository extends JpaRepository<Feedback, Long>{

    Feedback findByProposta(Proposta proposta);

    @Query("SELECT f"
            + " FROM Feedback f "
            + " JOIN f.proposta b "
            + " JOIN b.trabalho j "
            + " JOIN j.autor u"
            + " WHERE u = :usuario ")
    List<Feedback> findByCliente(@Param("usuario") Usuario usuario);
    


    @Query("SELECT f"
        + " FROM Feedback f "
        + " JOIN f.proposta b "
        + " JOIN b.trabalho j "
        + " WHERE j = :trabalho ")
    Feedback findByTrabalho(@Param("trabalho") Trabalho trabalho);

    List<Feedback> findByProposta(List<Proposta> propostas);
}
