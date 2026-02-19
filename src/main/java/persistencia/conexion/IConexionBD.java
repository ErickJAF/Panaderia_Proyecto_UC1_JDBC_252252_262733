/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.conexion;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author ERICK
 */
public interface IConexionBD {
    Connection crearConexion() throws SQLException;

}
