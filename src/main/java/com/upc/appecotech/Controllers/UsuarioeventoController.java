package com.upc.appecotech.controllers;

import com.upc.appecotech.entidades.Usuarioevento;
import com.upc.appecotech.interfaces.IUsuarioEventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UsuarioeventoController {

    @Autowired
    private IUsuarioEventoService usuarioEventoService;

    @GetMapping("/usuarioeventos")
    public List<Usuarioevento> listarUsuarioeventos(){
        return usuarioEventoService.listarUsuarioeventos();
    }

    @PostMapping("/usuarioEvento")
    public Usuarioevento registrarUsuarioEvento(@RequestBody Usuarioevento usuarioevento) {
        return usuarioEventoService.registrarUsuarioEvento(usuarioevento);
    }
}
