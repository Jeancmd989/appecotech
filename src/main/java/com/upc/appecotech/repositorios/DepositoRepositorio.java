package com.upc.appecotech.repositorios;

import com.upc.appecotech.entidades.Deposito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDepositoRepositorio extends JpaRepository<Deposito, Long> {
    List<Deposito> findByEstado(String estado);
    List<Deposito> findByusuarioId(Long id);
}
