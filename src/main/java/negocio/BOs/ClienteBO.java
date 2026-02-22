/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import java.time.LocalDate;
import java.util.logging.Logger;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.IClienteDAO;
import persistencia.dominio.Cliente;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author ERICK
 */
public class ClienteBO implements IClienteBO{

    private final IClienteDAO clienteDAO;

    private static final Logger LOG =
            Logger.getLogger(ClienteBO.class.getName());

    public ClienteBO(IClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    @Override
    public void registrarCliente(Cliente cliente) throws NegocioException {

        LOG.info("Intentando registrar cliente");

        validarCliente(cliente);

        try {
            clienteDAO.insertar(cliente);
        } catch (PersistenciaException e) {
            LOG.severe("Error al registrar cliente");
            throw new NegocioException("No se pudo registrar el cliente.", e);
        }

        LOG.info("Cliente registrado correctamente.");
    }

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

        if (cliente.getNumero() <= 0) {
            throw new NegocioException("El número debe ser mayor a 0.");
        }
    }
}
