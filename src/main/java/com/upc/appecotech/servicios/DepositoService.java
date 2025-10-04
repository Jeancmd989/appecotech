package com.upc.appecotech.servicios;

import com.upc.appecotech.dtos.DepositoDTO;
import com.upc.appecotech.entidades.Deposito;
import com.upc.appecotech.entidades.Historialdepunto;
import com.upc.appecotech.entidades.Usuario;
import com.upc.appecotech.interfaces.IDepositoService;
import com.upc.appecotech.repositorios.DepositoRepositorio;
import com.upc.appecotech.repositorios.HistorialPuntosRepository;
import com.upc.appecotech.repositorios.UsuarioRepositorio;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;


@Service
public class DepositoService implements IDepositoService{
    @Autowired
    private DepositoRepositorio depositoRepositorio;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private HistorialPuntosRepository historialPuntosRepository;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;



    @Override
    @Transactional
    public DepositoDTO registrarDeposito(DepositoDTO depositoDTO) {
        try {
            Usuario usuario = usuarioRepositorio.findById(depositoDTO.getIdUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + depositoDTO.getIdUsuario()));

            Deposito deposito = new Deposito();
            deposito.setIdusuario(usuario);
            deposito.setUbicacion(depositoDTO.getUbicacion());
            deposito.setFechaenvio(LocalDate.now());
            deposito.setDescripcion(depositoDTO.getDescripcion());
            deposito.setTiporesiduo(depositoDTO.getTiporesiduo());
            deposito.setUbicaciondeposito(depositoDTO.getUbicaciondeposito());
            deposito.setPruebas(depositoDTO.getPruebas());
            deposito.setCantidad(depositoDTO.getCantidad());
            deposito.setEstado("Pendiente");
            deposito.setPuntosotorgados(0);

            Deposito depositoGuardado = depositoRepositorio.save(deposito);

            return modelMapper.map(depositoGuardado, DepositoDTO.class);

        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al registrar depósito: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public DepositoDTO buscarPorId(Long id) {
        return depositoRepositorio.findById(id)
                .map(deposito -> modelMapper.map(deposito,DepositoDTO.class))
                .orElse(null);
    }


    @Override
    @Transactional
    public List<DepositoDTO> findAll() {
        List<Deposito> lista  = depositoRepositorio.findAll();
        return lista.stream().
                map(deposito -> modelMapper.map(deposito, DepositoDTO.class)).toList();
    }

    @Override
    @Transactional
    public DepositoDTO actualizarDeposito(Long id, DepositoDTO depositoDTO) {
        return depositoRepositorio.findById(id)
                .map(depositoExistente -> {
                    // Actualización campo por campo
                    if (depositoDTO.getUbicacion() != null) {
                        depositoExistente.setUbicacion(depositoDTO.getUbicacion());
                    }
                    if (depositoDTO.getDescripcion() != null) {
                        depositoExistente.setDescripcion(depositoDTO.getDescripcion());
                    }
                    if (depositoDTO.getTiporesiduo() != null) {
                        depositoExistente.setTiporesiduo(depositoDTO.getTiporesiduo());
                    }
                    if (depositoDTO.getUbicaciondeposito() != null) {
                        depositoExistente.setUbicaciondeposito(depositoDTO.getUbicaciondeposito());
                    }
                    if (depositoDTO.getPruebas() != null) {
                        depositoExistente.setPruebas(depositoDTO.getPruebas());
                    }
                    if (depositoDTO.getCantidad() != null) {
                        depositoExistente.setCantidad(depositoDTO.getCantidad());
                    }

                    Deposito actualizado = depositoRepositorio.save(depositoExistente);
                    return modelMapper.map(actualizado, DepositoDTO.class);
                })
                .orElseThrow(() -> new EntityNotFoundException("Depósito no encontrado con ID: " + id));
    }



    @Override
    @Transactional
    public DepositoDTO validarDeposito(Long id, boolean aprobado) {
        Deposito deposito = depositoRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Depósito no encontrado con ID: " + id));

        if (aprobado) {
            int puntos = calcularPuntos(deposito);
            deposito.setEstado("Aprobado");
            deposito.setPuntosotorgados(puntos);

            Historialdepunto historialdepunto = new Historialdepunto();
            historialdepunto.setIdusuario(deposito.getIdusuario());
            historialdepunto.setPuntosobtenidos(puntos);
            historialdepunto.setTipomovimiento("Deposito");
            historialdepunto.setDescripcion(deposito.getDescripcion());
            historialdepunto.setPuntoscanjeados(0);
            historialdepunto.setFecha(LocalDate.now());

            historialPuntosRepository.save(historialdepunto);
        } else {
            deposito.setEstado("Rechazado");
            deposito.setPuntosotorgados(0);
        }

        Deposito actualizado = depositoRepositorio.save(deposito);
        return modelMapper.map(actualizado, DepositoDTO.class);
    }


    private int calcularPuntos(Deposito deposito) {
    return deposito.getCantidad().intValue() * 10;
}
}

