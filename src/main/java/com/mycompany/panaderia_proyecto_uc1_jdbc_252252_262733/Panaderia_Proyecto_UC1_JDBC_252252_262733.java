/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.panaderia_proyecto_uc1_jdbc_252252_262733;

import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;
import presentacion.FrmCrearPedidoProgramado;

/**
 *
 * @author ERICK
 */
public class Panaderia_Proyecto_UC1_JDBC_252252_262733 {

    public static void main(String[] args) {
      
    

        IConexionBD conexion = new ConexionBD();

        FrmCrearPedidoProgramado frm =
                new FrmCrearPedidoProgramado(conexion);

        frm.setVisible(true);
    }
}
