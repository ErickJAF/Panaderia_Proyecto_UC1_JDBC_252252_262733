/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import negocio.DTOs.ClienteDTO;
import negocio.excepciones.NegocioException;
import persistencia.dominio.Cliente;

/**
 *
 * @author ERICK
 */
public interface IClienteBO {

  String registrarCliente(ClienteDTO cliente)
        throws NegocioException;

    void actualizarCliente(Cliente cliente) throws NegocioException;

    Cliente buscarPorId(int idCliente) throws NegocioException;
}
