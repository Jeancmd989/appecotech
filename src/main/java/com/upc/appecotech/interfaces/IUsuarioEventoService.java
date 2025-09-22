package com.upc.appecotech.interfaces;

import com.upc.appecotech.dto.UsuarioEventoDTO;
import com.upc.appecotech.entidades.Usuarioevento;

import java.util.List;

public interface IUsuarioEventoService {
    public UsuarioEventoDTO registrarUsuarioEvento(UsuarioEventoDTO usuarioEventoDTO);
    public UsuarioEventoDTO marcarAsistencia(Long idUsuarioEvento, boolean asistio);
    public List<UsuarioEventoDTO> listarInscritosPorEvento(Long idEvento);
    public UsuarioEventoDTO buscarPorId(Long id);
    public List<UsuarioEventoDTO> listarTodos();
}
