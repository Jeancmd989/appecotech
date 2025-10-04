package com.upc.appecotech.repositorios;

import com.upc.appecotech.entidades.Deposito;
import org.springframework.data.jpa.repository.JpaRepository;



public interface DepositoRepositorio extends JpaRepository<Deposito, Long> {
    void deleteByIdusuario_Id(Long idusuario);

}
