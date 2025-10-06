package com.upc.appecotech.repositorios;

import com.upc.appecotech.entidades.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface EventoRepositorio extends JpaRepository<Evento, Long> {
    List<Evento> findByFechaGreaterThanEqualOrderByFechaAsc(LocalDate fecha);

    List<Evento> findByFechaLessThanOrderByFechaDesc(LocalDate fecha);

}
