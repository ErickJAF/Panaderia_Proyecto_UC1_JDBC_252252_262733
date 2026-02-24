/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import negocio.BOs.IUsuarioBO;
import negocio.DTOs.UsuarioDTO;
import negocio.BOs.Sesion;
import negocio.excepciones.NegocioException;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author icoro
 */
    public class FrmLogin extends JFrame {

    private final IUsuarioBO usuarioBO;

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public FrmLogin(IUsuarioBO usuarioBO) {
        this.usuarioBO = usuarioBO;
        inicializar();
    }

    private void inicializar() {

        setTitle("Iniciar Sesión - Sistema Panadería");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        panelPrincipal.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("INICIAR SESIÓN");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelPrincipal.add(lblTitulo, gbc);

        gbc.gridwidth = 1;

        JLabel lblUsuario = new JLabel("Usuario:");
        gbc.gridy = 1;
        gbc.gridx = 0;
        panelPrincipal.add(lblUsuario, gbc);

        txtUsuario = new JTextField();
        gbc.gridx = 1;
        panelPrincipal.add(txtUsuario, gbc);

        JLabel lblPassword = new JLabel("Contraseña:");
        gbc.gridy = 2;
        gbc.gridx = 0;
        panelPrincipal.add(lblPassword, gbc);

        txtPassword = new JPasswordField();
        gbc.gridx = 1;
        panelPrincipal.add(txtPassword, gbc);

        btnLogin = new JButton("Ingresar");
        btnLogin.setBackground(new Color(0,123,255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panelPrincipal.add(btnLogin, gbc);

        add(panelPrincipal, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> iniciarSesion());
    }

    private void iniciarSesion() {

        try {
            String usuario = txtUsuario.getText();
            String password = new String(txtPassword.getPassword());

            UsuarioDTO usuarioDTO = usuarioBO.autenticar(usuario, password);

            Sesion.iniciarSesion(usuarioDTO);

            JOptionPane.showMessageDialog(this,
                    "Bienvenido " + usuarioDTO.getNombreUsuario());

            this.dispose();

            // Redirección por rol
if (usuarioDTO.getRol().equals("Empleado")) {
    new FrmPanelEmpleado(usuarioBO).setVisible(true);
} else {
    new FrmMenuCliente(usuarioBO).setVisible(true);
}

        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

