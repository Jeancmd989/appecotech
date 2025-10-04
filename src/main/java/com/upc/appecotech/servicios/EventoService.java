package com.upc.appecotech.servicios;

import com.upc.appecotech.entidades.Evento;
import com.upc.appecotech.entidades.Usuarioevento;
import com.upc.appecotech.interfaces.IEventoService;
import com.upc.appecotech.repositorios.EventoRepositorio;
import com.upc.appecotech.repositorios.UsuarioEventoRepositorio;
import com.upc.appecotech.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoService implements IEventoService {
    @Autowired
    private EventoRepositorio eventoRepositorio;
    private UsuarioEventoRepositorio usuarioEventoRepositorio;
    private UsuarioRepositorio usuarioRepositorio;


}
