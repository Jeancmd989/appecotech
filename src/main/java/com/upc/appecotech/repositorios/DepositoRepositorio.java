package com.upc.appecotech.repositorios;


import com.upc.appecotech.entidades.Deposito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface DepositoRepositorio extends JpaRepository<Deposito, Long> {
    @Query("SELECT d FROM Deposito d WHERE d.idusuario.id = :idUsuario ")
    List<Deposito> findByUsuarioId(@Param("idUsuario") Long idUsuario);

    void deleteByIdusuario_Id(Long idusuario);

}
