package com.upc.appecotech.servicios;

import com.upc.appecotech.dtos.UsuarioDTO;
import com.upc.appecotech.entidades.Metodopago;
import com.upc.appecotech.entidades.Suscripcion;
import com.upc.appecotech.security.entidades.Rol;
import com.upc.appecotech.security.entidades.Usuario;
import com.upc.appecotech.interfaces.IUsuarioService;
import com.upc.appecotech.repositorios.*;
import com.upc.appecotech.security.repositorios.RolRepositorio;
import com.upc.appecotech.security.repositorios.UsuarioRepositorio;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private FeedbackRepositorio feedbackRepositorio;

    @Autowired
    private ContactoRepositorio contactoRepositorio;

    @Autowired
    private UsuarioEventoRepositorio usuarioEventoRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MetodoPagoRepositorio metodoPagoRepositorio;

    @Autowired
    private SuscripcionRepositorio suscripcionRepositorio;

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private RolRepositorio rolRepositorio;


    @Override
    @Transactional
    public UsuarioDTO registrarUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioDTO.getId() != null) {
            throw new RuntimeException("No se debe proporcionar ID al crear un usuario");
        }
        if (usuarioRepositorio.existsByEmail(usuarioDTO.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Mapear el DTO a entidad
        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);

        // 🔒 Encriptar la contraseña
        usuario.setContraseña(bcrypt.encode(usuario.getContraseña()));

        // 👤 Asignar el rol por defecto "USER"
        Rol rolUser = rolRepositorio.findByNombrerol("USER")
                .orElseThrow(() -> new RuntimeException("No se encontró el rol USER"));
        usuario.getRoles().add(rolUser);

        // 💾 Guardar el usuario con la contraseña encriptada y rol
        Usuario guardado = usuarioRepositorio.save(usuario);

        // 🎁 Crear suscripción gratuita automáticamente
        crearSuscripcionBasicaGratuita(guardado.getId());

        return modelMapper.map(guardado, UsuarioDTO.class);
    }


    private void crearSuscripcionBasicaGratuita(Long idUsuario) {
        try {
            Metodopago metodoPagoGratuito = metodoPagoRepositorio.findByNombremetodo("Gratuito")
                    .orElseGet(() -> {
                        Metodopago nuevo = new Metodopago();
                        nuevo.setNombremetodo("Gratuito");
                        return metodoPagoRepositorio.save(nuevo);
                    });

            Usuario usuario = usuarioRepositorio.findById(idUsuario)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

            Suscripcion suscripcionBasica = new Suscripcion();
            suscripcionBasica.setIdusuario(usuario);
            suscripcionBasica.setIdmetodopago(metodoPagoGratuito);
            suscripcionBasica.setTipoplan("Basico");
            suscripcionBasica.setFechainicio(LocalDate.now());
            suscripcionBasica.setFechafin(LocalDate.now().plusYears(100)); // Permanente
            suscripcionBasica.setDescripcion("Plan Básico Gratuito - Acceso estándar");
            suscripcionBasica.setEstado("Activa");

            suscripcionRepositorio.save(suscripcionBasica);
        } catch (Exception e) {
            System.err.println("Error al crear suscripción básica: " + e.getMessage());
        }
    }



    @Override
    @Transactional
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
    @Transactional
    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + id + " no encontrado"));

        return modelMapper.map(usuario, UsuarioDTO.class);
    }


    @Override
    @Transactional
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
