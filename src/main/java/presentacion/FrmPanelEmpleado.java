package presentacion;

import negocio.BOs.Sesion;
import negocio.DTOs.UsuarioDTO;
import negocio.BOs.IUsuarioBO;
import negocio.BOs.IProductoBO;
import negocio.BOs.IPedidoExpressBO;

import javax.swing.*;
import java.awt.*;
import negocio.BOs.IPedidoProgramadoBO;

public class FrmPanelEmpleado extends JFrame {

    private final IUsuarioBO usuarioBO;
    private final IProductoBO productoBO;
    private final IPedidoExpressBO pedidoExpressBO;
    private final IPedidoProgramadoBO pedidoProgramadoBO;

    private JButton btnGestionDeEntregas;
    private JButton btnGestionProductos;
    private JButton btnCerrarSesion;

   public FrmPanelEmpleado(IUsuarioBO usuarioBO,
                        IProductoBO productoBO,
                        IPedidoExpressBO pedidoExpressBO,
                        IPedidoProgramadoBO pedidoProgramadoBO) {

    this.usuarioBO = usuarioBO;
    this.productoBO = productoBO;
    this.pedidoExpressBO = pedidoExpressBO;
    this.pedidoProgramadoBO = pedidoProgramadoBO;

    inicializar();
}

    private void inicializar() {

        UsuarioDTO usuario = Sesion.getUsuarioActual();

        setTitle("Panel Empleado - " + usuario.getNombreUsuario());
        setSize(450, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        panel.setBackground(Color.WHITE);

        btnGestionDeEntregas = crearBoton("Gestionar Entregas");
        btnGestionProductos = crearBoton("Gestionar Productos");
        btnCerrarSesion = crearBoton("Cerrar Sesión");

        panel.add(btnGestionDeEntregas);
        panel.add(btnGestionProductos);
        panel.add(btnCerrarSesion);

        add(panel, BorderLayout.CENTER);

        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        btnGestionDeEntregas.addActionListener(e -> abrirGestionEntregas());
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(new Color(40,167,69));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }
    
    private void abrirGestionEntregas() {
        dispose(); // Cierra el panel actual (opcional, pero recomendado)

        FrmGestionDeEntregas frm = new FrmGestionDeEntregas();
        frm.setVisible(true);
    }

    private void cerrarSesion() {

        Sesion.cerrarSesion();
        dispose();

        new FrmLogin(
        usuarioBO,
        productoBO,
        pedidoExpressBO,
        pedidoProgramadoBO
).setVisible(true);
    }
}