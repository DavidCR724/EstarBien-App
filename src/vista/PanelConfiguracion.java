/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vista;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import control.AdmDatos;
import control.AdministradorJpaController;
import control.MedicoJpaController;
import control.PacienteJpaController;
import control.Sesion;
import java.util.Date;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import modelo.Administrador;
import modelo.Medico;
import modelo.Paciente;
import modelo.Usuario;

/**
 *
 * @author carlo
 */
public class PanelConfiguracion extends javax.swing.JPanel {

    private AdmDatos admDatos;
    boolean modoOscuro = true;
    private Usuario usuarioGral;

    private Administrador admin;
    private Medico medico;
    private Paciente paciente;

    private AdministradorJpaController cAdmin;
    private MedicoJpaController cMedico;
    private PacienteJpaController cPaciente;

    private List<Administrador> administradores;
    private List<Medico> medicos;
    private List<Paciente> pacientes;

    /**
     * Creates new form PanelConfiguracion
     */
    public PanelConfiguracion() {
        initComponents();
        admDatos = new AdmDatos();
        cAdmin = new AdministradorJpaController(admDatos.getEmf());
        cMedico = new MedicoJpaController(admDatos.getEmf());
        cPaciente = new PacienteJpaController(admDatos.getEmf());
        administradores = cAdmin.findAdministradorEntities();
        medicos = cMedico.findMedicoEntities();
        pacientes = cPaciente.findPacienteEntities();

    }

    public void mostrarBienvenida(Usuario usuario) {
        String nombreCompleto = "";
        String usuarioNom = "";
        String estadoEs = "";
        String rol = usuario.getRol();

        // Determinar el nombre según el rol
        switch (rol.toLowerCase()) {
            case "administrador":
                if (usuario.getAdministrador() != null) {
                    buscarRol(usuario,"administrador");
                    usuarioNom = usuario.getNombreUsuario();
                    estadoEs = usuario.getActivo() ? "Activo":"No Activo";
                    usuarioGral = usuario;
                    
                    nombreCompleto  = admin.getNombre();

                }
                break;
            case "medico":
                if (usuario.getMedico() != null) {
                     buscarRol(usuario,"medico");
                    usuarioGral = usuario;
                    nombreCompleto  = medico.getNombre() + " " + medico.getApellidoPaterno() + " " + medico.getApellidoMaterno();
                    estadoEs = usuario.getActivo() ? "Activo":"No Activo";
                    usuarioNom = usuario.getNombreUsuario();
                }
                break;
            case "paciente":
                if (usuario.getPaciente() != null) {
                    buscarRol(usuario,"paciente");
                    nombreCompleto  = paciente.getNombre() + " " + paciente.getApellidoPaterno() + " " + paciente.getApellidoMaterno();
                    estadoEs = usuario.getActivo() ? "Activo":"No Activo";
                    usuarioNom = usuario.getNombreUsuario();
                    usuarioGral = usuario;
                    
                }
                break;
        }

        nombreText.setText("Hola! -  " + nombreCompleto  );
        nomUserText.setText("> Usuario " +usuarioNom + " <");
        ocupacionText.setText("Ocupacion: " + rol.substring(0, 1).toUpperCase() + rol.substring(1));
        estadoText.setText("Estado: "+estadoEs);

    }

    private void buscarRol(Usuario u, String rol) {
        switch (u.getRol()) {
            case "administrador":
                for (Administrador ad : administradores) {
                    if (ad.getIdUsuario().getIdUsuario() == u.getIdUsuario()) {
                        admin = ad;
                        break;
                    }
                }
                break;
            case "medico":
                for (Medico  ad : medicos) {
                    if (ad.getIdUsuario().getIdUsuario() == u.getIdUsuario()) {
                        medico = ad;
                        break;
                    }
                }
                break;
            case "paciente":
                for (Paciente  ad : pacientes) {
                    if (ad.getIdUsuario().getIdUsuario() == u.getIdUsuario()) {
                        paciente = ad;
                        break;
                    }
                }
                break;
        }

    }
    private void limpiarCampos(){
        nombreText.setText("Hola! - ");
        ocupacionText.setText("Ocupacion: ");
        estadoText.setText("Estado: ");
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        usuarioTexto = new javax.swing.JLabel();
        temaBoton = new javax.swing.JButton();
        nombreText = new javax.swing.JLabel();
        cerrarSesionBot = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        estadoText = new javax.swing.JLabel();
        ocupacionText = new javax.swing.JLabel();
        nomUserText = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1397, 882));
        setMinimumSize(new java.awt.Dimension(1397, 882));

        usuarioTexto.setBackground(new java.awt.Color(255, 255, 255));
        usuarioTexto.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        usuarioTexto.setText("Configuracion");

        temaBoton.setText("Tema Oscuro");
        temaBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                temaBotonActionPerformed(evt);
            }
        });

        nombreText.setFont(new java.awt.Font("Arial", 0, 36)); // NOI18N
        nombreText.setText("Hola! - ");

        cerrarSesionBot.setText("Cerrar Sesion");
        cerrarSesionBot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cerrarSesionBotActionPerformed(evt);
            }
        });

        estadoText.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        estadoText.setText("Estado: ");

        ocupacionText.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        ocupacionText.setText("Ocupacion:");

        nomUserText.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nomUserText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(estadoText, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ocupacionText, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 62, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(nomUserText)
                .addGap(18, 18, 18)
                .addComponent(ocupacionText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(estadoText)
                .addContainerGap(510, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(557, 557, 557)
                        .addComponent(usuarioTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(250, 250, 250)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(temaBoton)
                            .addComponent(cerrarSesionBot))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 354, Short.MAX_VALUE)
                .addComponent(nombreText, javax.swing.GroupLayout.PREFERRED_SIZE, 709, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(334, 334, 334))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(usuarioTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nombreText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(307, 307, 307)
                        .addComponent(temaBoton)
                        .addGap(18, 18, 18)
                        .addComponent(cerrarSesionBot))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(109, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void temaBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_temaBotonActionPerformed
        try {

            if (!modoOscuro) {
                FlatMacLightLaf.setup();
                UIManager.setLookAndFeel(new FlatMacLightLaf());
                temaBoton.setText("Tema Oscuro");
            } else {
                FlatDarculaLaf.setup();
                UIManager.setLookAndFeel(new FlatDarculaLaf());
                temaBoton.setText("Tema Claro");
            }

            modoOscuro = !modoOscuro;

            // Aplica el nuevo LookAndFeel a toda la ventana principal
            SwingUtilities.updateComponentTreeUI(SwingUtilities.getWindowAncestor(this));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_temaBotonActionPerformed

    private void cerrarSesionBotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cerrarSesionBotActionPerformed
        limpiarCampos();
        Sesion.cerrarSesion();
        MainView mainView = (MainView) SwingUtilities.getWindowAncestor(this);
        mainView.ocultarTodosLosMenus();
        mainView.mostrarPanel("IniciarSesion", mainView.panelIniSes);
    }//GEN-LAST:event_cerrarSesionBotActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cerrarSesionBot;
    private javax.swing.JLabel estadoText;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel nomUserText;
    private javax.swing.JLabel nombreText;
    private javax.swing.JLabel ocupacionText;
    private javax.swing.JButton temaBoton;
    private javax.swing.JLabel usuarioTexto;
    // End of variables declaration//GEN-END:variables
}
