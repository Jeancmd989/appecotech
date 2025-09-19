package com.upc.appecotech.controllers;


import com.upc.appecotech.entidades.Contacto;
import com.upc.appecotech.interfaces.IContactoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ContactoController {
    @Autowired
    private IContactoService contactoService;

    @GetMapping("/contactos")
    public List<Contacto> listarContactos()
    {return contactoService.listarContactos();}
}
