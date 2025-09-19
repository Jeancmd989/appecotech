package com.upc.appecotech.controllers;

import com.upc.appecotech.entidades.Suscripcion;
import com.upc.appecotech.interfaces.ISuscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SuscripcionController {
    @Autowired
    private ISuscripcionService suscripcionService;

    @GetMapping("/suscripciones")
    public List<Suscripcion> listarSuscripciones(){
        return suscripcionService.listarSuscripciones();
    }
}
