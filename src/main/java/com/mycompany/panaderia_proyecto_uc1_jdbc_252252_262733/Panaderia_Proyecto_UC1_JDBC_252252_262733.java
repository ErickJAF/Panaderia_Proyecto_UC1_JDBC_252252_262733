package com.mycompany.panaderia_proyecto_uc1_jdbc_252252_262733;

import javax.swing.SwingUtilities;
import negocio.BOs.IUsuarioBO;
import negocio.BOs.ProductoBO;
import negocio.BOs.UsuarioBO;
import persistencia.DAOs.IUsuarioDAO;
import persistencia.DAOs.UsuarioDAO;
import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;
import presentacion.FrmLogin;



public class Panaderia_Proyecto_UC1_JDBC_252252_262733 {
    
public static void main(String[] args) {

    IConexionBD conexion = new ConexionBD();
    IUsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
    IUsuarioBO usuarioBO = new UsuarioBO(usuarioDAO);

    SwingUtilities.invokeLater(() -> {
        new FrmLogin(usuarioBO).setVisible(true);
    });
}
}