/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pruebas;

import java.time.LocalDate;
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
import presentacion.FrmGestionClientes;

/**
 *
 * @author josed
 */

public class PruebaGUIClientes {

    public static void main(String[] args) {

        IConexionBD conexion = new ConexionBD();

        IClienteDAO clienteDAO = new ClienteDAO(conexion);
        IUsuarioDAO usuarioDAO = new UsuarioDAO(conexion);

        IClienteBO clienteBO = new ClienteBO(clienteDAO, usuarioDAO);

        SwingUtilities.invokeLater(() ->
                new FrmGestionClientes(clienteBO).setVisible(true)
        );
    }
}
