package com.upc.appecotech.Services;

import com.upc.appecotech.entidades.Contacto;
import com.upc.appecotech.interfaces.IContactoService;
import com.upc.appecotech.repositorios.ContactoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactoService implements IContactoService {
    @Autowired
    private ContactoRepositorio contactoRepositorio;


    @Override
    public List<Contacto> listarContactos() {
        return contactoRepositorio.findAll();
    }
}
