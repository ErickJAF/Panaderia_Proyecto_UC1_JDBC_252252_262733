/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import persistencia.dominio.Cliente;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author ERICK
 */
public interface IClienteDAO {
    void insertar(Cliente cliente) throws PersistenciaException;

    void actualizar(Cliente cliente) throws PersistenciaException;

    Cliente buscarPorId(int idCliente) throws PersistenciaException;
}
