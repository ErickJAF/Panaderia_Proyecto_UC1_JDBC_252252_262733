/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.*;

import negocio.BOs.IClienteBO;
import negocio.excepciones.NegocioException;
import persistencia.dominio.Cliente;
import persistencia.dominio.Cliente;

public class FrmGestionClientes extends JFrame {

    private final IClienteBO clienteBO;

    private JTextField txtNombre;
    private JTextField txtFechaNacimiento;
    private JTextField txtCalle;
    private JTextField txtColonia;
    private JTextField txtNumero;

    private JButton btnRegistrar;
    private JButton btnLimpiar;

    public FrmGestionClientes(IClienteBO clienteBO) {
        this.clienteBO = clienteBO;
        inicializarComponentes();
        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("Gestión de Clientes");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void inicializarComponentes() {

        setLayout(new GridLayout(7, 2, 5, 5));

        add(new JLabel("Nombre completo:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel("Fecha nacimiento (YYYY-MM-DD):"));
        txtFechaNacimiento = new JTextField();
        add(txtFechaNacimiento);

        add(new JLabel("Calle:"));
        txtCalle = new JTextField();
        add(txtCalle);

        add(new JLabel("Colonia:"));
        txtColonia = new JTextField();
        add(txtColonia);

        add(new JLabel("Número:"));
        txtNumero = new JTextField();
        add(txtNumero);

        btnRegistrar = new JButton("Registrar");
        btnLimpiar = new JButton("Limpiar");

        add(btnRegistrar);
        add(btnLimpiar);

        btnRegistrar.addActionListener(e -> registrarCliente());
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private void registrarCliente() {
//
//        try {
//
//            Cliente cliente = new Cliente(
//                    txtNombre.getText().trim(),
//                    LocalDate.parse(txtFechaNacimiento.getText().trim()),
//                    txtCalle.getText().trim(),
//                    txtColonia.getText().trim(),
//                    Integer.parseInt(txtNumero.getText().trim())
//            );
//
//            clienteBO.registrarCliente(cliente);
//
//            JOptionPane.showMessageDialog(this,
//                    "Cliente registrado correctamente.");
//
//            limpiarCampos();
//
//        } catch (NegocioException ex) {
//
//            JOptionPane.showMessageDialog(this,
//                    ex.getMessage(),
//                    "Error de negocio",
//                    JOptionPane.ERROR_MESSAGE);
//
//        } catch (Exception ex) {
//
//            JOptionPane.showMessageDialog(this,
//                    "Datos inválidos. Verifique formato.",
//                    "Error",
//                    JOptionPane.ERROR_MESSAGE);
//        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtFechaNacimiento.setText("");
        txtCalle.setText("");
        txtColonia.setText("");
        txtNumero.setText("");
    }
}
