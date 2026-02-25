/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pruebas;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import javax.swing.SwingUtilities;
import negocio.BOs.ClienteBO;
import negocio.BOs.IClienteBO;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.ClienteDAO;
import persistencia.DAOs.IClienteDAO;
import persistencia.DAOs.IUsuarioDAO;
import persistencia.DAOs.UsuarioDAO;
import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Cliente;


/**
 *
 * @author josed
 */

public class PruebaGUIClientes {

  public static void main(String[] args) {
    try {
        IConexionBD conexion = new ConexionBD();

        IUsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
        IClienteDAO clienteDAO = new ClienteDAO(conexion);

        IClienteBO clienteBO = new ClienteBO(clienteDAO, usuarioDAO);

        Cliente cliente = new Cliente(
                "Juan Pablo2",
                LocalDate.of(2002, 5, 10),
                "Av. Siempre Viva",
                "Centro",
                List.of("5551234567", "5559876543")
        );

        clienteBO.registrarCliente(cliente);

        System.out.println("✅ Cliente insertado correctamente");

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}

