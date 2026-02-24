/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pruebas;

import java.time.LocalDate;
import negocio.BOs.ClienteBO;
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
public class pruebaCliente {

   public static void main(String[] args) {

    try {

        IConexionBD conexion = new ConexionBD();

        IClienteDAO clienteDAO = new ClienteDAO(conexion);
        IUsuarioDAO usuarioDAO = new UsuarioDAO(conexion);

        ClienteBO clienteBO = new ClienteBO(clienteDAO, usuarioDAO);

        Cliente nuevo = new Cliente(
        "Pedro Lopez Nuevo",
        LocalDate.of(1999, 3, 15),
        "Calle 20",
        "Centro",
        55
        );

        clienteBO.registrarCliente(nuevo);

        System.out.println("Cliente registrado correctamente.");

    } catch (NegocioException e) {
        e.printStackTrace();
    }
}
}
