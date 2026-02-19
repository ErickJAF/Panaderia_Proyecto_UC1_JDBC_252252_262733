/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.panaderia_proyecto_uc1_jdbc_252252_262733;

import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;
import persistencia.DAOs.ProductoDAO;
import persistencia.DAOs.IProductoDAO;
import negocio.ProductoBO;
import negocio.IProductoBO;
import presentacion.FrmCrearPedidoProgramado;

/**
 *
 * @author ERICK
 */
public class Panaderia_Proyecto_UC1_JDBC_252252_262733 {

    public static void main(String[] args) {
      
    
IConexionBD conexion = new ConexionBD();

// DAO
IProductoDAO productoDAO = new ProductoDAO(conexion);

// BO
IProductoBO productoBO = new ProductoBO(productoDAO);

// FORM
FrmCrearPedidoProgramado frm =
        new FrmCrearPedidoProgramado(conexion, productoBO);

frm.setVisible(true);

    }
}
