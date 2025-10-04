package com.upc.appecotech.servicios;

import com.upc.appecotech.dtos.ContactoDTO;
import com.upc.appecotech.entidades.Contacto;
import com.upc.appecotech.entidades.Usuario;
import com.upc.appecotech.interfaces.IContactoService;
import com.upc.appecotech.repositorios.ContactoRepositorio;
import com.upc.appecotech.repositorios.UsuarioRepositorio;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContactoService implements IContactoService {
    @Autowired
    private ContactoRepositorio contactoRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    public ContactoDTO crearContacto(ContactoDTO contactoDTO) {
        try {
            Usuario usuario = usuarioRepositorio.findById(contactoDTO.getIdUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

            // Validaciones
            if (contactoDTO.getDescripcionProblema() == null || contactoDTO.getDescripcionProblema().trim().isEmpty()) {
                throw new RuntimeException("La descripción del problema no puede estar vacía");
            }

            Contacto contacto = new Contacto();
            contacto.setIdusuario(usuario);
            contacto.setDescripcionproblema(contactoDTO.getDescripcionProblema());
            contacto.setTiporeclamo(contactoDTO.getTipoReclamo());
            contacto.setFecha(LocalDate.now());
            contacto.setEstado("Pendiente"); // Estado inicial

            Contacto guardado = contactoRepositorio.save(contacto);

            // Mapeo manual
            ContactoDTO response = new ContactoDTO();
            response.setId(guardado.getId());
            response.setIdUsuario(guardado.getIdusuario().getId());
            response.setFecha(guardado.getFecha());
            response.setDescripcionProblema(guardado.getDescripcionproblema());
            response.setTipoReclamo(guardado.getTiporeclamo());
            response.setEstado(guardado.getEstado());

            return response;

        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al crear contacto: " + e.getMessage());
        }
    }

    @Override
    public ContactoDTO actualizarEstadoContacto(Long idContacto, String nuevoEstado) {
        Contacto contacto = contactoRepositorio.findById(idContacto)
                .orElseThrow(() -> new EntityNotFoundException("Contacto no encontrado"));

        contacto.setEstado(nuevoEstado);
        Contacto actualizado = contactoRepositorio.save(contacto);

        ContactoDTO response = new ContactoDTO();
        response.setId(actualizado.getId());
        response.setIdUsuario(actualizado.getIdusuario().getId());
        response.setFecha(actualizado.getFecha());
        response.setDescripcionProblema(actualizado.getDescripcionproblema());
        response.setTipoReclamo(actualizado.getTiporeclamo());
        response.setEstado(actualizado.getEstado());

        return response;
    }

    @Override
    public ContactoDTO buscarPorId(Long id) {
        return null;
    }

    @Override
    public List<ContactoDTO> listarTodos() {
        return List.of();
    }

    @Override
    public List<ContactoDTO> listarContactosPorUsuario(Long idUsuario) {
        return List.of();
    }

    @Override
    public List<ContactoDTO> listarContactosPorEstado(String estado) {
        return List.of();
    }
}
