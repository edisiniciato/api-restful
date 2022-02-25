package com.edi.apirestful.repository;

import com.edi.apirestful.model.Filme;
import com.edi.apirestful.model.FilmeVencedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmeRepository extends JpaRepository<Filme, Long> {

    @Query(value = "select distinct f.* from filme f " +
            "       inner join filme_vencedor fv on fv.filme_id = f.id " +
            "       order by f.year ", nativeQuery = true)
    List<Filme> findWinners();

    @Query(value = "select fv from FilmeVencedor fv where fv.filme = ?1")
    List<FilmeVencedor> findWinnersFilme(Filme idFilme);

}
