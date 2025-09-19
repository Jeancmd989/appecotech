package com.upc.appecotech.controllers;

import com.upc.appecotech.entidades.Metodopago;
import com.upc.appecotech.interfaces.IMetodopagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MetodopagoController {
    @Autowired
    private IMetodopagoService metodopagoService;

    @GetMapping("/metodopagos")
    public List<Metodopago> listarMetodopagos(){
        return metodopagoService.listarMetodopagos();
    }
}
