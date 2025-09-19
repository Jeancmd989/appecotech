package com.upc.appecotech.repositorios;

import com.upc.appecotech.entidades.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactoRepositorio extends JpaRepository<Contacto, Long> {
}
