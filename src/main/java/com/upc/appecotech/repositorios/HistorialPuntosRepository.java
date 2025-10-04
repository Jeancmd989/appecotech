package com.upc.appecotech.repositorios;

import com.upc.appecotech.entidades.Historialdepunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface HistorialPuntosRepository extends JpaRepository<Historialdepunto, Long> {

    @Query("SELECT h FROM Historialdepunto h WHERE h.idusuario.id = :idUsuario ORDER BY h.fecha DESC")
    List<Historialdepunto> findByUsuarioId(@Param("idUsuario") Long idUsuario);

    @Query("SELECT h FROM Historialdepunto h WHERE h.idusuario.id = :idUsuario AND h.tipomovimiento = :tipo ORDER BY h.fecha DESC")
    List<Historialdepunto> findByUsuarioIdAndTipo(@Param("idUsuario") Long idUsuario, @Param("tipo") String tipo);

    @Query("SELECT h FROM Historialdepunto h WHERE h.idusuario.id = :idUsuario AND h.fecha BETWEEN :inicio AND :fin ORDER BY h.fecha DESC")
    List<Historialdepunto> findByUsuarioIdAndFechaBetween(@Param("idUsuario") Long idUsuario, @Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    void deleteByIdusuario_Id(Long idusuario);
}
