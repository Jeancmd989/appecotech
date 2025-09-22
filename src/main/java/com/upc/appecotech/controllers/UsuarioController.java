package com.upc.appecotech.controllers;

import com.upc.appecotech.dto.UsuarioDTO;
import com.upc.appecotech.entidades.Usuario;
import com.upc.appecotech.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UsuarioController {
    @Autowired
    private IUsuarioService usuarioService;

    @PostMapping("/usuarios")
    public UsuarioDTO registrarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.registrarUsuario(usuarioDTO);
    }

    @PutMapping("/usuario/{id}")
    public UsuarioDTO ActualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.actualizarUsuario(id, usuarioDTO);
    }

    @GetMapping("/usuarios")
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

}
