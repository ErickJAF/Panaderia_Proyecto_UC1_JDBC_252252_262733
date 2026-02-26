/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import java.time.LocalDate;
import java.util.logging.Logger;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.IClienteDAO;
import persistencia.DAOs.IUsuarioDAO;
import persistencia.dominio.Cliente;
import persistencia.excepciones.PersistenciaException;

/**
 * Clase de lógica de negocio encargada de gestionar las operaciones
 * relacionadas con los clientes del sistema.
 * <p>
 * Esta clase valida la información antes de enviarla a la capa de persistencia,
 * y controla las reglas de negocio asociadas al registro, actualización,
 * búsqueda y desactivación de clientes.
 * </p>
 *
 * @author ERICK
 */
public class ClienteBO implements IClienteBO{

    /**
     * DAO encargado de las operaciones de persistencia de Cliente.
     */
    private final IClienteDAO clienteDAO;

    /**
     * DAO encargado de las operaciones de persistencia de Usuario.
     */
    private final IUsuarioDAO usuarioDAO;

    /**
     * Logger para el registro de eventos y seguimiento de operaciones.
     */
    private static final Logger LOG =
            Logger.getLogger(ClienteBO.class.getName());

  
    /**
     * Constructor que recibe las dependencias necesarias para la
     * gestión de clientes.
     *
     * @param clienteDAO DAO para operaciones de clientes
     * @param usuarioDAO DAO para operaciones de usuarios
     */
    public ClienteBO(IClienteDAO clienteDAO, IUsuarioDAO usuarioDAO) {
        this.clienteDAO = clienteDAO;
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * Registra un nuevo cliente en el sistema.
     * <p>
     * Realiza validaciones de negocio, genera automáticamente un usuario
     * asociado y posteriormente guarda el cliente en la base de datos.
     * </p>
     *
     * @param cliente Objeto Cliente con la información a registrar
     * @throws NegocioException Si ocurre un error en validaciones o persistencia
     */
   @Override
    public void registrarCliente(Cliente cliente) throws NegocioException {

        validarCliente(cliente);

        try {

            String username = cliente.getNombreCompleto()
                    .toLowerCase()
                    .replace(" ", ".");

            int idGenerado = usuarioDAO.insertar(
                    username,
                    "Cliente",
                    "1234"
            );

            cliente.setIdCliente(idGenerado);

            clienteDAO.insertar(cliente);

        } catch (PersistenciaException e) {
            throw new NegocioException("Error al registrar cliente completo.", e);
        }
    }

    /**
     * Actualiza la información de un cliente existente.
     *
     * @param cliente Objeto Cliente con la información actualizada
     * @throws NegocioException Si el ID es inválido, fallan las validaciones
     *                          o ocurre un error en persistencia
     */
    @Override
    public void actualizarCliente(Cliente cliente) throws NegocioException {

        LOG.info("Intentando actualizar cliente");

        if (cliente.getIdCliente() <= 0) {
            throw new NegocioException("ID de cliente inválido.");
        }

        validarCliente(cliente);

        try {
            clienteDAO.actualizar(cliente);
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudo actualizar el cliente.", e);
        }

        LOG.info("Cliente actualizado correctamente.");
    }

    /**
     * Busca un cliente por su identificador.
     *
     * @param idCliente ID del cliente a buscar
     * @return Cliente encontrado
     * @throws NegocioException Si el ID es inválido, no existe el cliente
     *                          o ocurre un error en persistencia
     */
    @Override
    public Cliente buscarPorId(int idCliente) throws NegocioException {

        if (idCliente <= 0) {
            throw new NegocioException("ID inválido.");
        }

        try {
            Cliente cliente = clienteDAO.buscarPorId(idCliente);

            if (cliente == null) {
                throw new NegocioException("Cliente no encontrado.");
            }

            return cliente;

        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar cliente.", e);
        }
    }

    /**
     * Valida que los datos del cliente cumplan con las reglas de negocio.
     *
     * @param cliente Cliente a validar
     * @throws NegocioException Si algún dato obligatorio es inválido o no cumple
     *                          con las reglas establecidas
     */
    private void validarCliente(Cliente cliente) throws NegocioException {

        if (cliente.getNombreCompleto() == null
                || cliente.getNombreCompleto().isBlank()) {

            throw new NegocioException("El nombre completo es obligatorio.");
        }

        if (cliente.getFechaNacimiento() == null) {
            throw new NegocioException("La fecha de nacimiento es obligatoria.");
        }

        if (cliente.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new NegocioException("La fecha de nacimiento no puede ser futura.");
        }

        if (cliente.getCalle() == null || cliente.getCalle().isBlank()) {
            throw new NegocioException("La calle es obligatoria.");
        }

        if (cliente.getColonia() == null || cliente.getColonia().isBlank()) {
            throw new NegocioException("La colonia es obligatoria.");
        }

        if (cliente.getTelefonos() == null || cliente.getTelefonos().isEmpty()) {

            throw new NegocioException("Debe registrar al menos un número de teléfono.");
        }

        for (String telefono : cliente.getTelefonos()) {

            if (telefono == null || telefono.isBlank()) {
                throw new NegocioException("No puede haber teléfonos vacíos.");
            }

            if (!telefono.matches("\\d{7,15}")) {
                throw new NegocioException(
                        "El teléfono debe contener solo números y tener entre 7 y 15 dígitos.");
            }
        }
    }

    /**
     * Desactiva un cliente en el sistema.
     * <p>
     * Verifica la existencia del cliente y posteriormente desactiva
     * el usuario asociado.
     * </p>
     *
     * @param idCliente ID del cliente a desactivar
     * @throws NegocioException Si el cliente no existe o ocurre un error
     *                          en la capa de persistencia
     */
    public void desactivarCliente(int idCliente)
        throws NegocioException {

        try {

            Cliente cliente = clienteDAO.buscarPorId(idCliente);

            if (cliente == null) {
                throw new NegocioException("Cliente no existe");
            }

            usuarioDAO.desactivarUsuario(idCliente);

        } catch (PersistenciaException e) {
            throw new NegocioException(
                    "Error al desactivar cliente", e);
        }
    }
}