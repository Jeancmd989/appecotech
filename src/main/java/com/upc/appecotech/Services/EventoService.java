package com.upc.appecotech.Services;

import com.upc.appecotech.entidades.Evento;
import com.upc.appecotech.entidades.Usuario;
import com.upc.appecotech.entidades.Usuarioevento;
import com.upc.appecotech.interfaces.IEventoService;
import com.upc.appecotech.repositorios.EventoRepositorio;
import com.upc.appecotech.repositorios.UsuarioEventoRepositorio;
import com.upc.appecotech.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoService implements IEventoService {
    @Autowired
    private EventoRepositorio eventoRepositorio;
    private UsuarioEventoRepositorio usuarioEventoRepositorio;
    private UsuarioRepositorio usuarioRepositorio;


    @Override
    public Evento registrarEvento(Evento evento) {
        return eventoRepositorio.save(evento);
    }

    @Override
    public List<Evento> listarEventos() {
        return eventoRepositorio.findAll();
    }

    @Override
    public Evento buscareventoPorId(Long id) {
        return eventoRepositorio.findById(id).orElseThrow();
    }

    @Override
    public Usuarioevento incribirIsuarioevento(long idUsuario, long idEvento) {
        return null;
    }

    @Override
    public Usuarioevento marcarAsistencia(long idUsuarioEvento, boolean asistio) {
        return null;
    }
}
