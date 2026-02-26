/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */





/**
 *
 * @author josed
 */

package pruebas;

import javax.swing.SwingUtilities;
import negocio.BOs.ClienteBO;
import persistencia.DAOs.ClienteDAO;
import persistencia.DAOs.IClienteDAO;
import persistencia.DAOs.IUsuarioDAO;
import persistencia.DAOs.UsuarioDAO;
import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;

//import presentacion.FrmGestionCliente;
//
//public class PruebaGUIClientes {
//
//    public static void main(String[] args) {
//
//        SwingUtilities.invokeLater(() -> {
//
//            // Crear conexión
//            IConexionBD conexion = new ConexionBD();
//
//            // Crear DAOs
//            IClienteDAO clienteDAO = new ClienteDAO(conexion);
//            IUsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
//
//            // Crear BO
//            ClienteBO clienteBO = 
//                    new ClienteBO(clienteDAO, usuarioDAO);
//
//            // Inyectar BO en el Frame
//            FrmGestionCliente frm =
//                    new FrmGestionCliente(clienteBO);
//
//            frm.setLocationRelativeTo(null);
//            frm.setVisible(true);
//
//        });
//    }
//}
