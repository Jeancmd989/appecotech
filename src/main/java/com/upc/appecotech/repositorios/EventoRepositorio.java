package com.upc.appecotech.repositorios;

import com.upc.appecotech.entidades.Evento;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public interface EventoRepositorio extends JpaRepository<Evento, Long> {
    List<Evento> findByFechaGreaterThanEqualOrderByFechaAsc(LocalDate fecha);

    List<Evento> findByFechaLessThanOrderByFechaDesc(LocalDate fecha);

    @Query("SELECT e.nombre, COUNT(ue), e.fecha " +
            "FROM Evento e " +
            "LEFT JOIN Usuarioevento ue ON ue.idevento.id = e.id " +
            "GROUP BY e.id, e.nombre, e.fecha " +
            "ORDER BY COUNT(ue) DESC")
    List<Object[]> obtenerEventosMasParticipados(Pageable pageable);
}

