package com.upc.appecotech.interfaces;

import com.upc.appecotech.entidades.Evento;
import com.upc.appecotech.entidades.Usuarioevento;

import java.util.List;

public interface IEventoService {
    public Evento registrarEvento(Evento evento);
    public List<Evento> listarEventos();
    public Evento buscareventoPorId(Long id);
    public Usuarioevento incribirIsuarioevento(long idUsuario, long idEvento);
    public Usuarioevento marcarAsistencia(long idUsuarioEvento, boolean asistio);
}
