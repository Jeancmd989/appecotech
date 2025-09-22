package com.upc.appecotech.repositorios;

import com.upc.appecotech.entidades.Canjeusuario;
import com.upc.appecotech.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CanjeUsuarioRepositorio extends JpaRepository<Canjeusuario, Long> {
    @Query("SELECT c FROM Canjeusuario c WHERE c.idusuario.id = :idUsuario")
    List<Canjeusuario> findByUsuarioId(@Param("idUsuario") Long idUsuario);
}
