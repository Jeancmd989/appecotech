package com.upc.appecotech.interfaces;


import com.upc.appecotech.dto.UsuarioDTO;
import com.upc.appecotech.entidades.Usuario;

import java.util.List;

public interface IUsuarioService {
    public UsuarioDTO registrarUsuario(UsuarioDTO usuarioDTO);
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO);
    public UsuarioDTO buscarPorId(Long id);
    public List<UsuarioDTO> listarUsuarios();
}
