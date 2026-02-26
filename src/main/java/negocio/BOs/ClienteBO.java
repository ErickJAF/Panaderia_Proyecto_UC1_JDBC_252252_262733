/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import negocio.DTOs.ClienteDTO;
import negocio.DTOs.TelefonoDTO;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.IClienteDAO;
import persistencia.DAOs.IUsuarioDAO;
import persistencia.dominio.Cliente;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author ERICK
 */
public class ClienteBO implements IClienteBO{

    private final IClienteDAO clienteDAO;
    private final IUsuarioDAO usuarioDAO;

    private static final Logger LOG =
            Logger.getLogger(ClienteBO.class.getName());

  
    
    public ClienteBO(IClienteDAO clienteDAO, IUsuarioDAO usuarioDAO) {
    this.clienteDAO = clienteDAO;
    this.usuarioDAO = usuarioDAO;
}

    /**
     *
     * @param dto
     * @throws NegocioException
     */
    @Override
public String registrarCliente(ClienteDTO dto)
        throws NegocioException {

    try {

        // Validaciones básicas
        if (dto.getNombreCompleto() == null || dto.getNombreCompleto().isBlank()) {
            throw new NegocioException("Nombre obligatorio");
        }

        if (dto.getTelefonos() == null || dto.getTelefonos().isEmpty()) {
            throw new NegocioException("Debe agregar al menos un teléfono");
        }

        if (dto.getEdad() < 18) {
            throw new NegocioException("Debe ser mayor de edad");
        }

        // Generar usuario automático
        String base = dto.getNombreCompleto()
                .substring(0, 3)
                .toLowerCase();

        String usuarioGenerado = base + ".cli";

        // Insertar usuario
        usuarioDAO.insertar(
                usuarioGenerado,
                "CLIENTE",
                dto.getContrasena()
        );

        // Convertir DTO → Dominio
        Cliente cliente = new Cliente();
        cliente.setNombreCompleto(dto.getNombreCompleto());
        cliente.setFechaNacimiento(dto.getFechaNacimiento());
        cliente.setCalle("SIN CALLE");
        cliente.setColonia("SIN COLONIA");

        // Convertir teléfonos
        List<String> telefonos = new ArrayList<>();

        for (TelefonoDTO tel : dto.getTelefonos()) {
            telefonos.add(tel.getNumero());
        }

        cliente.setTelefonos(telefonos);

        // Insertar cliente
        clienteDAO.insertar(cliente);

        
        return usuarioGenerado;

    } catch (Exception e) {

        throw new NegocioException(
                "Error al registrar cliente: " + e.getMessage());
    }
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
