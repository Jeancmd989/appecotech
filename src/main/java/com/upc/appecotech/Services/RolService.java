package com.upc.appecotech.Services;

import com.upc.appecotech.entidades.Rol;
import com.upc.appecotech.interfaces.IRolService;
import com.upc.appecotech.repositorios.RolRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService implements IRolService {
    @Autowired
    private RolRepositorio rolService;

    @Override
    public List<Rol> listarRoles() {
        return  rolService.findAll();
    }
}
