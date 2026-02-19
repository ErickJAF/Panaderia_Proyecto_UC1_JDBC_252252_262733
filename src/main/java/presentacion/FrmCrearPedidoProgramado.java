/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import dto.PedidoProgramadoDTO;
import negocio.PedidoProgramadoBO;
import persistencia.conexion.IConexionBD;
import persistencia.excepciones.PersistenciaException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 *
 * @author icoro
 */
public class FrmCrearPedidoProgramado extends JFrame{
       private JTable tablaProductos;
    private JTextField txtIdCliente;
    private JTextField txtIdEmpleado;
    private JTextField txtCupon;
    private JButton btnAgregarPedido;

    private DefaultTableModel modelo;

    private final PedidoProgramadoBO pedidoBO;

    public FrmCrearPedidoProgramado(IConexionBD conexionBD) {
        this.pedidoBO = new PedidoProgramadoBO(conexionBD);
        inicializar();
        cargarProductosPrueba();
    }

    private void inicializar() {

        setTitle("Crear Pedido Programado");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel();
        modelo.addColumn("Producto");
        modelo.addColumn("Precio");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Nota");

        tablaProductos = new JTable(modelo);

        add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new GridLayout(2,4,10,10));

        panelInferior.add(new JLabel("ID Cliente:"));
        txtIdCliente = new JTextField();
        panelInferior.add(txtIdCliente);

        panelInferior.add(new JLabel("ID Empleado:"));
        txtIdEmpleado = new JTextField();
        panelInferior.add(txtIdEmpleado);

        panelInferior.add(new JLabel("Cupón (opcional):"));
        txtCupon = new JTextField();
        panelInferior.add(txtCupon);

        btnAgregarPedido = new JButton("Agregar Pedido");
        panelInferior.add(new JLabel());
        panelInferior.add(btnAgregarPedido);

        add(panelInferior, BorderLayout.SOUTH);

        btnAgregarPedido.addActionListener(e -> crearPedido());
    }

    private void cargarProductosPrueba() {

        modelo.addRow(new Object[]{"Concha", 18.00, 0, ""});
        modelo.addRow(new Object[]{"Bolillo", 10.00, 0, ""});
        modelo.addRow(new Object[]{"Rol de Canela", 25.00, 0, ""});
    }

    private void crearPedido() {

        try {

            double subtotal = 0;

            for(int i=0; i<modelo.getRowCount(); i++) {
                int cantidad = Integer.parseInt(modelo.getValueAt(i,2).toString());
                double precio = Double.parseDouble(modelo.getValueAt(i,1).toString());
                subtotal += cantidad * precio;
            }

            double descuento = 0;
            double total = subtotal - descuento;

            int idCliente = Integer.parseInt(txtIdCliente.getText());
            int idEmpleado = Integer.parseInt(txtIdEmpleado.getText());

            Integer idCupon = null;
            if(!txtCupon.getText().isBlank()){
                idCupon = Integer.parseInt(txtCupon.getText());
                descuento = 10; // simulación
                total = subtotal - descuento;
            }

            PedidoProgramadoDTO dto =
                    new PedidoProgramadoDTO(
                            idCliente,
                            idEmpleado,
                            subtotal,
                            descuento,
                            total,
                            idCupon
                    );

            pedidoBO.crearPedidoProgramado(dto);

            JOptionPane.showMessageDialog(this,
                    "Pedido creado correctamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "Verifica cantidades e IDs",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);

        } catch (PersistenciaException ex) {

            JOptionPane.showMessageDialog(this,
                    "Error BD:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
