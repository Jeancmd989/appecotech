package com.upc.appecotech.controllers;

import com.upc.appecotech.entidades.Historialdepunto;
import com.upc.appecotech.interfaces.IHistorialdepuntoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HistorialdepuntoController {
    @Autowired
    private IHistorialdepuntoService historialdepuntoService;

    @GetMapping("/historialdepuntos")
    public List<Historialdepunto> getHistorialdepuntos() {
        return historialdepuntoService.listarHistorialdepuntos();
    }
}
