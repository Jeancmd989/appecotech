package com.upc.appecotech.repositorios;

import com.upc.appecotech.entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepositorio extends JpaRepository<Producto, Long> {
    List<Producto> findAllByOrderByPuntosrequeridoAsc();
    List<Producto> findAllByOrderByPuntosrequeridoDesc();
    List<Producto> findByPuntosrequeridoBetween(Integer min, Integer max);
}
