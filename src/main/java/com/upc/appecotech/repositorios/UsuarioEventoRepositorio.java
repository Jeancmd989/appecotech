package com.upc.appecotech.repositorios;


import com.upc.appecotech.entidades.Usuarioevento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioEventoRepositorio extends JpaRepository<Usuarioevento, Long> {

    boolean existsByIdusuario_IdAndIdevento_Id(Long idUsuario, Long idEvento);

    List<Usuarioevento> findByIdevento_Id(Long idEvento);

}
