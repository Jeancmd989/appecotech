package com.upc.appecotech.controladores;

import com.upc.appecotech.entidades.Usuariorol;
import com.upc.appecotech.interfaces.IUsuariorolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UsuariorolController {
    @Autowired
    private IUsuariorolService usuariorolService;

    @GetMapping("/usuarioroles")
    public List<Usuariorol> listarUsuariorol(){
        return usuariorolService.listarUsuariorols();
    }
}
