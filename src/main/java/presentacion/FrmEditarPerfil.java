package presentacion;

import negocio.BOs.IUsuarioBO;
import negocio.BOs.Sesion;
import negocio.DTOs.UsuarioDTO;
import negocio.excepciones.NegocioException;

import javax.swing.*;
import java.awt.*;

public class FrmEditarPerfil extends JFrame {

    private final IUsuarioBO usuarioBO;

    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JPasswordField txtPassword;

    public FrmEditarPerfil(IUsuarioBO usuarioBO) {
        this.usuarioBO = usuarioBO;
        inicializar();
    }

    private void inicializar() {

        UsuarioDTO usuario = Sesion.getUsuarioActual();

        setTitle("Editar Perfil");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6,2,10,10));

        add(new JLabel("Nombre:"));
        txtNombre = new JTextField(usuario.getNombreUsuario());
        add(txtNombre);

        add(new JLabel("Teléfono:"));
    

        add(new JLabel("Correo:"));
      

        add(new JLabel("Nueva contraseña:"));
        txtPassword = new JPasswordField();
        add(txtPassword);

        JButton btnGuardar = new JButton("Guardar");
        add(btnGuardar);

        btnGuardar.addActionListener(e -> guardarCambios());
    }

    private void guardarCambios() {

        try {

            UsuarioDTO usuario = Sesion.getUsuarioActual();

            usuarioBO.editarPerfil(
                    usuario.getId(),
                    txtNombre.getText(),
                    txtTelefono.getText(),
                    txtCorreo.getText(),
                    new String(txtPassword.getPassword())
            );

            JOptionPane.showMessageDialog(this,
                    "Perfil actualizado correctamente");

            dispose();

        } catch (NegocioException ex) {

            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}