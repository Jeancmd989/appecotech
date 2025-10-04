package com.upc.appecotech.servicios;

import com.upc.appecotech.entidades.Metodopago;
import com.upc.appecotech.interfaces.IMetodopagoService;
import com.upc.appecotech.repositorios.MetodoPagoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetodopagoService implements IMetodopagoService {
    @Autowired
    private MetodoPagoRepositorio metodoPagoRepositorio;

    @Override
    public List<Metodopago> listarMetodopagos() {
        return   metodoPagoRepositorio.findAll();
    }
}
