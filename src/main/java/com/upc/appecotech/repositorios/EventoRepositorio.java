package com.upc.appecotech.repositorios;

import com.upc.appecotech.entidades.Evento;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventoRepositorio extends JpaRepository<Evento, Long> {
}
