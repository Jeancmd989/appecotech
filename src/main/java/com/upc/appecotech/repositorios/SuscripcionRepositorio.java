package com.upc.appecotech.repositorios;

import com.upc.appecotech.entidades.Suscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface SuscripcionRepositorio extends JpaRepository<Suscripcion, Long> {
    @Query("SELECT s FROM Suscripcion s WHERE s.idusuario.id = :idUsuario")
    List<Suscripcion> findByUsuarioId(@Param("idUsuario") Long idUsuario);
    void deleteByIdusuario_Id(Long idusuario);
}
