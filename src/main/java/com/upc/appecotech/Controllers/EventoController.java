package com.upc.appecotech.controllers;


import com.upc.appecotech.entidades.Evento;
import com.upc.appecotech.interfaces.IEventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EventoController {
    @Autowired
    private IEventoService eventoService;

    @GetMapping("/eventos")
    public List<Evento> listarEventos()
    {
        return eventoService.listarEventos();
    }
}
