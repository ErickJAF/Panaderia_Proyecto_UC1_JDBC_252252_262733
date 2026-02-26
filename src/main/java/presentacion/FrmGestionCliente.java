/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;
//
//import java.awt.GridLayout;
//
//import javax.swing.*;
//
//
//import persistencia.dominio.Cliente;
//
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//import negocio.BOs.ClienteBO;
//
//public class FrmGestionCliente extends JFrame {
//
//    private JTextField txtId;
//    private JTextField txtNombre;
//    private JTextField txtFecha;
//    private JTextField txtCalle;
//    private JTextField txtColonia;
//    private JTextField txtTelefonos;
//
//    private ClienteBO clienteBO; // Asegúrate de inyectarlo
//
//    public FrmGestionCliente(ClienteBO clienteBO) {
//        this.clienteBO = clienteBO;
//        inicializarComponentes();
//    }
//
//    private void inicializarComponentes() {
//
//        setTitle("Gestión de Clientes");
//        setSize(500, 400);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        JPanel panel = new JPanel();
//        panel.setLayout(new GridLayout(8, 2, 10, 10));
//        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//
//        panel.add(new JLabel("ID Cliente:"));
//        txtId = new JTextField();
//        panel.add(txtId);
//
//        panel.add(new JLabel("Nombre completo:"));
//        txtNombre = new JTextField();
//        panel.add(txtNombre);
//
//        panel.add(new JLabel("Fecha Nacimiento (YYYY-MM-DD):"));
//        txtFecha = new JTextField();
//        panel.add(txtFecha);
//
//        panel.add(new JLabel("Calle:"));
//        txtCalle = new JTextField();
//        panel.add(txtCalle);
//
//        panel.add(new JLabel("Colonia:"));
//        txtColonia = new JTextField();
//        panel.add(txtColonia);
//
//        panel.add(new JLabel("Teléfonos (separados por coma):"));
//        txtTelefonos = new JTextField();
//        panel.add(txtTelefonos);
//
//        JButton btnRegistrar = new JButton("Registrar");
//        JButton btnActualizar = new JButton("Actualizar");
//        JButton btnEliminar = new JButton("Dar de Baja");
//        JButton btnBuscar = new JButton("Buscar");
//
//        panel.add(btnRegistrar);
//        panel.add(btnActualizar);
//        panel.add(btnEliminar);
//        panel.add(btnBuscar);
//
//        add(panel);
//
//        // Eventos
//        btnRegistrar.addActionListener(e -> registrarCliente());
//        btnActualizar.addActionListener(e -> actualizarCliente());
//        btnEliminar.addActionListener(e -> darDeBaja());
//        btnBuscar.addActionListener(e -> buscarCliente());
//    }
//
//    private void registrarCliente() {
//
//        try {
//            Cliente cliente = obtenerDatosFormulario();
//            clienteBO.registrarCliente(cliente);
//            JOptionPane.showMessageDialog(this,
//                    "Cliente registrado correctamente");
//            limpiarCampos();
//
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this,
//                    e.getMessage(),
//                    "Error",
//                    JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void actualizarCliente() {
//
//        try {
//            Cliente cliente = obtenerDatosFormulario();
//            clienteBO.actualizarCliente(cliente);
//            JOptionPane.showMessageDialog(this,
//                    "Cliente actualizado correctamente");
//
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this,
//                    e.getMessage(),
//                    "Error",
//                    JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void darDeBaja() {
//
//        int confirm = JOptionPane.showConfirmDialog(this,
//                "¿Seguro que desea desactivar el cliente?",
//                "Confirmar",
//                JOptionPane.YES_NO_OPTION);
//
//        if (confirm == JOptionPane.YES_OPTION) {
//            try {
//                int id = Integer.parseInt(txtId.getText());
//                clienteBO.desactivarCliente(id);
//                JOptionPane.showMessageDialog(this,
//                        "Cliente desactivado correctamente");
//                limpiarCampos();
//            } catch (Exception e) {
//                JOptionPane.showMessageDialog(this,
//                        e.getMessage(),
//                        "Error",
//                        JOptionPane.ERROR_MESSAGE);
//            }
//        }
//    }
//
//    private void buscarCliente() {
//
//        try {
//            int id = Integer.parseInt(txtId.getText());
//            Cliente cliente = clienteBO.buscarPorId(id);
//
//            if (cliente == null) {
//                JOptionPane.showMessageDialog(this,
//                        "Cliente no encontrado");
//                return;
//            }
//
//            txtNombre.setText(cliente.getNombreCompleto());
//            txtFecha.setText(cliente.getFechaNacimiento().toString());
//            txtCalle.setText(cliente.getCalle());
//            txtColonia.setText(cliente.getColonia());
//            txtTelefonos.setText(
//                    String.join(",", cliente.getTelefonos()));
//
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this,
//                    e.getMessage(),
//                    "Error",
//                    JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private Cliente obtenerDatosFormulario() throws Exception {
//
//        if (txtId.getText().isEmpty() ||
//            txtNombre.getText().isEmpty() ||
//            txtFecha.getText().isEmpty() ||
//            txtCalle.getText().isEmpty() ||
//            txtColonia.getText().isEmpty()) {
//
//            throw new Exception("Todos los campos son obligatorios");
//        }
//
//        int id = Integer.parseInt(txtId.getText());
//        LocalDate fecha = LocalDate.parse(txtFecha.getText());
//
//        List<String> telefonos = Arrays.asList(
//        txtTelefonos.getText().split(","));
//
//for (String tel : telefonos) {
//
//    String limpio = tel.trim();
//
//    if (!limpio.matches("\\d{10}")) {
//        throw new Exception(
//            "Los teléfonos deben tener 10 dígitos numéricos");
//    }
//}
//
//        Cliente cliente = new Cliente();
//        cliente.setIdCliente(id);
//        cliente.setNombreCompleto(txtNombre.getText());
//        cliente.setFechaNacimiento(fecha);
//        cliente.setCalle(txtCalle.getText());
//        cliente.setColonia(txtColonia.getText());
//        cliente.setTelefonos(telefonos);
//
//        return cliente;
//    }
//
//    private void limpiarCampos() {
//        txtId.setText("");
//        txtNombre.setText("");
//        txtFecha.setText("");
//        txtCalle.setText("");
//        txtColonia.setText("");
//        txtTelefonos.setText("");
//    }
//}