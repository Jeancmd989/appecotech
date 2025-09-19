package com.upc.appecotech.repositorios;

import com.upc.appecotech.entidades.Suscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface SuscripcionRepositorio extends JpaRepository<Suscripcion, Long> {
}
