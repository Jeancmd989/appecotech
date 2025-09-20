package com.upc.appecotech.repositorios;

import com.upc.appecotech.entidades.Evento;
import com.upc.appecotech.entidades.Usuario;
import com.upc.appecotech.entidades.Usuarioevento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioEventoRepositorio extends JpaRepository<Usuarioevento, Long> {
    boolean existsByUsuarioIdUsuarioAndEventoIdEvento(Long idUsuario, Long idEvento);

    public List<Usuarioevento> findByIdEvento(Evento idEvento);
}
