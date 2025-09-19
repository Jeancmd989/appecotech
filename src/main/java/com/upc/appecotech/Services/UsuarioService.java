package com.upc.appecotech.Services;

import com.upc.appecotech.dto.DepositoDTO;
import com.upc.appecotech.dto.UsuarioDTO;
import com.upc.appecotech.entidades.Deposito;
import com.upc.appecotech.entidades.Usuario;
import com.upc.appecotech.interfaces.IUsuarioService;
import com.upc.appecotech.repositorios.UsuarioRepositorio;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService implements IUsuarioService {
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public UsuarioDTO registrarUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioDTO.getIdUsuario()==null){
            Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);
            usuarioRepositorio.save(usuario);
            return modelMapper.map(usuario, UsuarioDTO.class);
        }
        return null;
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

}
