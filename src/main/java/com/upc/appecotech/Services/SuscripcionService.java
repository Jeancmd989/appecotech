package com.upc.appecotech.Services;

import com.upc.appecotech.entidades.Suscripcion;
import com.upc.appecotech.interfaces.ISuscripcionService;
import com.upc.appecotech.repositorios.SuscripcionRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuscripcionService implements ISuscripcionService {
    @Autowired
    private SuscripcionRepositorio suscripcionRepositorio;

    @Override
    public List<Suscripcion> listarSuscripciones() {
        return  suscripcionRepositorio.findAll();
    }
}
