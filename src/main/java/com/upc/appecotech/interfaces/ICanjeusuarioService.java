package com.upc.appecotech.interfaces;

import com.upc.appecotech.dtos.CanjeUsuarioDTO;

import java.util.List;

public interface ICanjeusuarioService {
    CanjeUsuarioDTO canjearProducto(CanjeUsuarioDTO canjeUsuarioDTO);
    boolean validarPuntosUsuario(Long idUsuario, Long idProducto, Integer cantidad);
    CanjeUsuarioDTO buscarPorId(Long id);
    List<CanjeUsuarioDTO> listarTodos();
    List<CanjeUsuarioDTO> listarCanjesPorUsuario(Long idUsuario);
    int obtenerPuntosDisponibles(Long idUsuario);

}
