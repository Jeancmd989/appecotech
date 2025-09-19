package com.upc.appecotech.Services;

import com.upc.appecotech.entidades.Canjeusuario;
import com.upc.appecotech.interfaces.ICanjeusuarioService;
import com.upc.appecotech.repositorios.CanjeUsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CanjeusuarioService implements ICanjeusuarioService {
    @Autowired
    private CanjeUsuarioRepositorio canjeUsuarioRepositorio;


    @Override
    public List<Canjeusuario> listarCanjeUsuarios() {
        return canjeUsuarioRepositorio.findAll();
    }
}
