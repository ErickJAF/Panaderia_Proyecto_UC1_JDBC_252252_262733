/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import dto.DetallePedidoDTO;
import dto.PedidoProgramadoDTO;
import dto.ProductoDTO;
import negocio.IProductoBO;
import negocio.IPedidoProgramadoBO;
import persistencia.conexion.IConexionBD;
import persistencia.excepciones.PersistenciaException;
import negocio.PedidoProgramadoBO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
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

    private final IPedidoProgramadoBO pedidoBO;
    private final IProductoBO productoBO;

    public FrmCrearPedidoProgramado(IConexionBD conexionBD, IProductoBO productoBO) {

       this.pedidoBO = new PedidoProgramadoBO(conexionBD);
        this.productoBO = productoBO;

        inicializar();
        cargarProductosBD();
    }

    private void inicializar() {

        setTitle("Crear Pedido Programado");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(
                new Object[]{"ID", "Producto", "Precio", "Cantidad", "Nota"}, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4;
            }
        };

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

    private void cargarProductosBD() {

        modelo.setRowCount(0);

        try {

            List<ProductoDTO> lista = productoBO.obtenerDisponibles();

            for (ProductoDTO p : lista) {

                modelo.addRow(new Object[]{
                        p.getIdProducto(),
                        p.getNombre(),
                        p.getPrecio(),
                        0,
                        ""
                });
            }

        } catch (PersistenciaException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void crearPedido() {

        try {

            List<DetallePedidoDTO> detalles = new ArrayList<>();

            double subtotal = 0;

            for (int i = 0; i < modelo.getRowCount(); i++) {

    Object cantidadObj = modelo.getValueAt(i,3);

    if(cantidadObj == null || cantidadObj.toString().isBlank())
        continue;

    int cantidad = Integer.parseInt(cantidadObj.toString());

    if (cantidad > 0) {

        int idProducto = Integer.parseInt(modelo.getValueAt(i,0).toString());
        double precio = Double.parseDouble(modelo.getValueAt(i,2).toString());

        String nota = "";
        Object notaObj = modelo.getValueAt(i,4);

        if(notaObj != null)
            nota = notaObj.toString();

        subtotal += cantidad * precio;

        detalles.add(
            new DetallePedidoDTO(idProducto, cantidad, precio, nota)
        );
    }
}


            if(detalles.isEmpty()){
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar al menos un producto",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            double descuento = 0;
            double total = subtotal;

            int idCliente = Integer.parseInt(txtIdCliente.getText());
            int idEmpleado = Integer.parseInt(txtIdEmpleado.getText());

            Integer idCupon = null;

            if(!txtCupon.getText().isBlank()){
                idCupon = Integer.parseInt(txtCupon.getText());
                descuento = 10;
                total = subtotal - descuento;
            }

            PedidoProgramadoDTO dto =
                    new PedidoProgramadoDTO(
                            idCliente,
                            idEmpleado,
                            subtotal,
                            descuento,
                            total,
                            idCupon,
                            detalles
                    );

            pedidoBO.crearPedidoProgramado(dto);

            JOptionPane.showMessageDialog(this,
                    "Pedido creado correctamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
