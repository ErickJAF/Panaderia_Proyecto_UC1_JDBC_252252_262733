/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

/**
 *
 * @author josed
 */

import javax.swing.*;
import java.awt.*;
import java.time.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Arrays;


import javax.swing.table.DefaultTableModel;

import java.time.ZoneId;

import negocio.BOs.ClienteBO;
import negocio.DTOs.ClienteDTO;
import negocio.DTOs.TelefonoDTO;
import negocio.excepciones.NegocioException;

import persistencia.DAOs.ClienteDAO;
import persistencia.DAOs.UsuarioDAO;
import persistencia.conexion.ConexionBD;

public class FrmRegistroCliente extends JFrame {

    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmar;
    private JSpinner spinnerFecha;

    private JTable tablaTelefonos;
    private DefaultTableModel modeloTelefonos;

    private JButton btnAgregarTelefono;
    private JButton btnEliminarTelefono;
    private JButton btnFinalizar;
    private JButton btnCancelar;
    private FrmLogin frmLogin;

    public FrmRegistroCliente(FrmLogin login) {
    this.frmLogin = login;
    setTitle("Registro de Cliente");
    setSize(600, 550);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    inicializarComponentes();
}

    private void inicializarComponentes() {

        JPanel panelPrincipal = new JPanel();
panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitulo = new JLabel("REGISTRO DE CLIENTE", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;


        txtNombre = new JTextField();
        txtCorreo = new JTextField();
        txtPassword = new JPasswordField();
        txtConfirmar = new JPasswordField();

        spinnerFecha = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinnerFecha, "dd/MM/yyyy");
        spinnerFecha.setEditor(editor);

        int y = 0;

        gbc.gridx = 0; gbc.gridy = y;
        panelForm.add(new JLabel("Nombre completo:"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtNombre, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panelForm.add(new JLabel("Fecha nacimiento:"), gbc);
        gbc.gridx = 1;
        panelForm.add(spinnerFecha, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panelForm.add(new JLabel("Correo:"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtCorreo, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panelForm.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtPassword, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panelForm.add(new JLabel("Confirmar contraseña:"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtConfirmar, gbc);

        panelPrincipal.add(panelForm);
panelPrincipal.add(Box.createVerticalStrut(15));


        // ---------------- TELEFONOS ----------------

        modeloTelefonos = new DefaultTableModel();
        modeloTelefonos.addColumn("Tipo");
        modeloTelefonos.addColumn("Número");

        tablaTelefonos = new JTable(modeloTelefonos);
        JScrollPane scrollTabla = new JScrollPane(tablaTelefonos);
        scrollTabla.setPreferredSize(new Dimension(500, 120));

        btnAgregarTelefono = new JButton("Agregar Teléfono");
        btnEliminarTelefono = new JButton("Eliminar Teléfono");

        JPanel panelTelefonos = new JPanel(new BorderLayout());
        panelTelefonos.setBorder(BorderFactory.createTitledBorder("Teléfonos"));
        panelTelefonos.add(scrollTabla, BorderLayout.CENTER);

        JPanel panelBtnsTel = new JPanel();
        panelBtnsTel.add(btnAgregarTelefono);
        panelBtnsTel.add(btnEliminarTelefono);

        panelTelefonos.add(panelBtnsTel, BorderLayout.SOUTH);

        panelPrincipal.add(panelTelefonos);

        add(panelPrincipal, BorderLayout.CENTER);

        // ---------------- BOTONES FINALES ----------------

        btnFinalizar = new JButton("Finalizar Registro");
        btnCancelar = new JButton("Cancelar");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnFinalizar);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        // ---------------- EVENTOS ----------------

        btnAgregarTelefono.addActionListener(e -> agregarTelefono());
        btnEliminarTelefono.addActionListener(e -> eliminarTelefono());
        btnFinalizar.addActionListener(e -> finalizarRegistro());
        btnCancelar.addActionListener(e -> regresarLogin());
    }

    private void agregarTelefono() {

        String[] tipos = {"Casa", "Trabajo", "Personal"};

        String tipo = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione tipo:",
                "Tipo de Teléfono",
                JOptionPane.QUESTION_MESSAGE,
                null,
                tipos,
                tipos[0]
        );

        if (tipo == null) return;

        String numero = JOptionPane.showInputDialog("Ingrese número:");

        if (numero != null && !numero.isBlank()) {
            modeloTelefonos.addRow(new Object[]{tipo, numero});
        }
    }

    private void eliminarTelefono() {
        int fila = tablaTelefonos.getSelectedRow();
        if (fila >= 0) {
            modeloTelefonos.removeRow(fila);
        }
    }

    private void finalizarRegistro() {

        try {

            if (txtNombre.getText().isBlank()
                    || txtCorreo.getText().isBlank()
                    || txtPassword.getPassword().length == 0
                    || txtConfirmar.getPassword().length == 0) {

                JOptionPane.showMessageDialog(this,
                        "Todos los campos son obligatorios");
                return;
            }

            if (!Arrays.equals(txtPassword.getPassword(),
                    txtConfirmar.getPassword())) {

                JOptionPane.showMessageDialog(this,
                        "Las contraseñas no coinciden");
                return;
            }

            if (modeloTelefonos.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Debe agregar al menos un teléfono");
                return;
            }

            Date fecha = (Date) spinnerFecha.getValue();
            LocalDate fechaNacimiento = fecha.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            java.util.List<TelefonoDTO> listaTelefonos = new ArrayList<>();

            for (int i = 0; i < modeloTelefonos.getRowCount(); i++) {
                String tipo = modeloTelefonos.getValueAt(i, 0).toString();
                String numero = modeloTelefonos.getValueAt(i, 1).toString();
                listaTelefonos.add(new TelefonoDTO(numero, tipo));
            }

            ClienteDTO cliente = new ClienteDTO(
                    txtNombre.getText().trim(),
                    txtCorreo.getText().trim(),
                    fechaNacimiento,
                    new String(txtPassword.getPassword()),
                    listaTelefonos
            );

            ClienteBO clienteBO = new ClienteBO(
                    new ClienteDAO(new ConexionBD()),
                    new UsuarioDAO(new ConexionBD())
            );

            String usuarioGenerado = clienteBO.registrarCliente(cliente);

JOptionPane.showMessageDialog(this,
            """
            Registro exitoso.
            
            Tu usuario es: """ + usuarioGenerado
        + "\nGuárdalo para iniciar sesión.",
        "Registro completado",
        JOptionPane.INFORMATION_MESSAGE);

            frmLogin.setVisible(true);
           dispose();
           

        } catch (NegocioException ex) {

            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this,
                    "Error inesperado: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void regresarLogin() {
       frmLogin.setVisible(true);
dispose();
    }
}