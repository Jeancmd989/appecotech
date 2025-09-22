package com.upc.appecotech.repositorios;

import com.upc.appecotech.entidades.Historialdepunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistorialPuntosRepository extends JpaRepository<Historialdepunto, Long> {
    @Query("SELECT h FROM Historialdepunto h WHERE h.idusuario.id = :idUsuario")
    List<Historialdepunto> findByUsuarioId(@Param("idUsuario") Long idUsuario);
}
