package com.upc.appecotech.controllers;

import com.upc.appecotech.entidades.Canjeusuario;
import com.upc.appecotech.interfaces.ICanjeusuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CanjeusuarioController {
    @Autowired
    private ICanjeusuarioService canjeusuarioService;

    @GetMapping("/canjeusuarios")
    public List<Canjeusuario> listarCanjeusuario()
    {return canjeusuarioService.listarCanjeUsuarios();}

}
