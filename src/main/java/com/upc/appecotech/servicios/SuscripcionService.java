package com.upc.appecotech.servicios;

import com.upc.appecotech.dtos.SuscripcionDTO;
import com.upc.appecotech.entidades.Metodopago;
import com.upc.appecotech.entidades.Suscripcion;
import com.upc.appecotech.security.entidades.Usuario;
import com.upc.appecotech.interfaces.ISuscripcionService;
import com.upc.appecotech.repositorios.MetodoPagoRepositorio;
import com.upc.appecotech.repositorios.SuscripcionRepositorio;
import com.upc.appecotech.security.repositorios.UsuarioRepositorio;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SuscripcionService implements ISuscripcionService {
    @Autowired
    private SuscripcionRepositorio suscripcionRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private MetodoPagoRepositorio metodoPagoRepositorio;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    @Transactional
    public SuscripcionDTO crearSuscripcion(SuscripcionDTO suscripcionDTO) {
        try {
            Usuario usuario = usuarioRepositorio.findById(suscripcionDTO.getIdusuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

            Metodopago metodoPago = metodoPagoRepositorio.findById(suscripcionDTO.getIdmetodopago())
                    .orElseThrow(() -> new EntityNotFoundException("Método de pago no encontrado"));


            List<Suscripcion> suscripcionesActivas = suscripcionRepositorio.findByUsuarioId(usuario.getId());
            for (Suscripcion sus : suscripcionesActivas) {
                if ("Activa".equals(sus.getEstado())) {
                    sus.setEstado("Reemplazada");
                    suscripcionRepositorio.save(sus);
                }
            }

            Suscripcion suscripcion = new Suscripcion();
            suscripcion.setIdusuario(usuario);
            suscripcion.setIdmetodopago(metodoPago);
            suscripcion.setTipoplan(suscripcionDTO.getTipoplan());
            suscripcion.setFechainicio(LocalDate.now());
            suscripcion.setFechafin(calcularFechaFin(suscripcionDTO.getTipoplan()));
            suscripcion.setDescripcion(generarDescripcion(suscripcionDTO.getTipoplan()));
            suscripcion.setEstado("Activa");

            Suscripcion guardada = suscripcionRepositorio.save(suscripcion);

            return modelMapper.map(guardada, SuscripcionDTO.class);

        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al crear suscripción: " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public boolean validarSuscripcionActiva(Long idUsuario) {
        List<Suscripcion> suscripciones = suscripcionRepositorio.findByUsuarioId(idUsuario);

        return suscripciones.stream()
                .anyMatch(s -> "Activa".equals(s.getEstado()) &&
                        s.getFechafin().isAfter(LocalDate.now()));
    }

    @Override
    @Transactional
    public SuscripcionDTO actualizarEstadoSuscripcion(Long idSuscripcion, String nuevoEstado) {
        return suscripcionRepositorio.findById(idSuscripcion)
                .map(suscripcion -> {
                    suscripcion.setEstado(nuevoEstado);
                    Suscripcion actualizada = suscripcionRepositorio.save(suscripcion);
                    return modelMapper.map(actualizada, SuscripcionDTO.class);
                })
                .orElseThrow(() -> new EntityNotFoundException("Suscripción no encontrada con ID: " + idSuscripcion));
    }

    @Override
    @Transactional
    public SuscripcionDTO renovarSuscripcion(Long idSuscripcion, String nuevoPlan) {
        return suscripcionRepositorio.findById(idSuscripcion)
                .map(suscripcion -> {
                    suscripcion.setTipoplan(nuevoPlan);
                    suscripcion.setFechainicio(LocalDate.now());
                    suscripcion.setFechafin(calcularFechaFin(nuevoPlan));
                    suscripcion.setDescripcion(generarDescripcion(nuevoPlan));
                    suscripcion.setEstado("Activa");

                    Suscripcion renovada = suscripcionRepositorio.save(suscripcion);
                    return modelMapper.map(renovada, SuscripcionDTO.class);
                })
                .orElseThrow(() -> new EntityNotFoundException("Suscripción no encontrada con ID: " + idSuscripcion));
    }

    @Override
    public SuscripcionDTO buscarPorId(Long id) {
        return suscripcionRepositorio.findById(id)
                .map(suscripcion -> modelMapper.map(suscripcion, SuscripcionDTO.class))
                .orElse(null);
    }

    @Override
    public List<SuscripcionDTO> listarTodas() {
        List<Suscripcion> lista = suscripcionRepositorio.findAll();
        return lista.stream()
                .map(suscripcion -> modelMapper.map(suscripcion, SuscripcionDTO.class))
                .toList();
    }

    @Override
    public List<SuscripcionDTO> listarSuscripcionesPorUsuario(Long idUsuario) {
        usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + idUsuario));

        List<Suscripcion> lista = suscripcionRepositorio.findByUsuarioId(idUsuario);

        return lista.stream()
                .map(suscripcion -> modelMapper.map(suscripcion, SuscripcionDTO.class))
                .toList();
    }

    @Override
    public SuscripcionDTO obtenerSuscripcionActivaUsuario(Long idUsuario) {
        List<Suscripcion> suscripciones = suscripcionRepositorio.findByUsuarioId(idUsuario);

        return suscripciones.stream()
                .filter(s -> "Activa".equals(s.getEstado()) && s.getFechafin().isAfter(LocalDate.now()))
                .findFirst()
                .map(suscripcion -> modelMapper.map(suscripcion, SuscripcionDTO.class))
                .orElse(null);
    }

    @Override
    @Transactional
    public Integer obtenerMultiplicadorPuntos(Long idUsuario) {
        SuscripcionDTO suscripcionActiva = obtenerSuscripcionActivaUsuario(idUsuario);

        if (suscripcionActiva == null) {
            return 1;
        }

        return switch (suscripcionActiva.getTipoplan().toLowerCase()) {
            case "basico" -> 1;
            case "premium" -> 2;
            case "vip" -> 3;
            default -> 1;
        };
    }

    private LocalDate calcularFechaFin(String tipoPlan) {
        LocalDate ahora = LocalDate.now();

        return switch (tipoPlan.toLowerCase()) {
            case "basico" -> ahora.plusMonths(1);
            case "premium" -> ahora.plusMonths(6);
            case "vip" -> ahora.plusYears(1);
            default -> ahora.plusMonths(1);
        };
    }

    private String generarDescripcion(String tipoPlan) {
        return switch (tipoPlan.toLowerCase()) {
            case "basico" -> "Plan Básico - Acceso estándar a funcionalidades";
            case "premium" -> "Plan Premium - Beneficios adicionales y más ecoPuntos";
            case "vip" -> "Plan VIP - Acceso completo y beneficios exclusivos";
            default -> "Plan estándar";
        };

    }

}
