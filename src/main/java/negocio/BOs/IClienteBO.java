/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import negocio.excepciones.NegocioException;
import persistencia.dominio.Cliente;

/**
 * Interfaz que define las operaciones de negocio relacionadas
 * con la gestión de clientes.
 * <p>
 * Establece los métodos que deben implementarse para el registro,
 * actualización y consulta de clientes dentro del sistema.
 * </p>
 * 
 * @author ERICK
 */
public interface IClienteBO {

    /**
     * Registra un nuevo cliente en el sistema.
     *
     * @param cliente Objeto Cliente con la información a registrar
     * @throws NegocioException Si ocurre un error en las validaciones
     *                          o en la operación de registro
     */
    void registrarCliente(Cliente cliente) throws NegocioException;

    /**
     * Actualiza la información de un cliente existente.
     *
     * @param cliente Objeto Cliente con los datos actualizados
     * @throws NegocioException Si ocurre un error en las validaciones
     *                          o en la operación de actualización
     */
    void actualizarCliente(Cliente cliente) throws NegocioException;

    /**
     * Busca un cliente por su identificador.
     *
     * @param idCliente Identificador único del cliente
     * @return Cliente correspondiente al ID proporcionado
     * @throws NegocioException Si el ID es inválido o ocurre un error
     *                          durante la búsqueda
     */
    Cliente buscarPorId(int idCliente) throws NegocioException;
}