package com.upc.appecotech.interfaces;

import com.upc.appecotech.dto.UsuarioEventoDTO;
import com.upc.appecotech.entidades.Usuarioevento;

import java.util.List;

public interface IUsuarioEventoService {
    public List<Usuarioevento> listarUsuarioeventos();
    public UsuarioEventoDTO resgistrarUsuarioEvento(Long idUsuario, Long idEvento);
    public UsuarioEventoDTO marcarAsistencia(Long idUsuarioEvento, boolean asistio);
    public List<UsuarioEventoDTO> listarInscritosPorEvento(Long idEvento);
}
