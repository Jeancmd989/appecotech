package com.upc.appecotech.interfaces;

import com.upc.appecotech.dto.DepositoDTO;
import com.upc.appecotech.entidades.Deposito;

import java.util.List;

public interface IDepositoService {
    public DepositoDTO registrarDeposito(DepositoDTO depositoDTO);
    public DepositoDTO buscarPorId(Long id);
    public List<DepositoDTO> findAll();
    public DepositoDTO actualizarDeposito(Long id, DepositoDTO depositoDTO);
    public DepositoDTO validarDeposito(Long id, boolean aprobado);
}
