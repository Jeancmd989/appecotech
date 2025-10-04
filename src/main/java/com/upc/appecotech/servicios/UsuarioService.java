package com.upc.appecotech.servicios;

import com.upc.appecotech.dtos.UsuarioDTO;
import com.upc.appecotech.entidades.Usuario;
import com.upc.appecotech.interfaces.IUsuarioService;
import com.upc.appecotech.repositorios.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService implements IUsuarioService {
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private CanjeUsuarioRepositorio canjeUsuarioRepositorio;

    @Autowired
    private HistorialPuntosRepository historialPuntosRepository;

    @Autowired
    private DepositoRepositorio depositoRepositorio;

    @Autowired
    private SuscripcionRepositorio suscripcionRepositorio;

    @Autowired
    private FeedbackRepositorio feedbackRepositorio;

    @Autowired
    private ContactoRepositorio contactoRepositorio;

    @Autowired
    private UsuarioEventoRepositorio usuarioEventoRepositorio;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public UsuarioDTO registrarUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioDTO.getIdUsuario() != null) {
            throw new RuntimeException("No se debe proporcionar ID al crear un usuario");
        }
        if (usuarioRepositorio.existsByEmail(usuarioDTO.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);
        Usuario guardado = usuarioRepositorio.save(usuario);

        return modelMapper.map(guardado, UsuarioDTO.class);
    }



    @Override
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        return usuarioRepositorio.findById(id)
                .map(usuarioExistente -> {
                    usuarioExistente.setNombre(usuarioDTO.getNombre());
                    usuarioExistente.setApellidos(usuarioDTO.getApellidos());
                    usuarioExistente.setEmail(usuarioDTO.getEmail());
                    usuarioExistente.setTelefono(usuarioDTO.getTelefono());
                    usuarioExistente.setDireccion(usuarioDTO.getDireccion());
                    usuarioExistente.setContraseña(usuarioDTO.getContraseña());

                    Usuario actualizado = usuarioRepositorio.save(usuarioExistente);
                    return modelMapper.map(actualizado, UsuarioDTO.class);
                })
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }


    @Override
    public UsuarioDTO buscarPorId(Long id) {
        return usuarioRepositorio.findById(id)
                .map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
                .orElse(null);
    }


    @Override
    public List<UsuarioDTO> listarUsuarios() {
        List<Usuario> lista = usuarioRepositorio.findAll();
        return lista.stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));


        canjeUsuarioRepositorio.deleteByIdusuario_Id(id);
        feedbackRepositorio.deleteByIdusuario_Id(id);
        contactoRepositorio.deleteByIdusuario_Id(id);
        suscripcionRepositorio.deleteByIdusuario_Id(id);
        usuarioEventoRepositorio.deleteByIdusuario_Id(id);
        historialPuntosRepository.deleteByIdusuario_Id(id);
        depositoRepositorio.deleteByIdusuario_Id(id);


        usuarioRepositorio.deleteById(id);
    }

}
