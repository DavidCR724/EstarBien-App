/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vista;

import control.UsuarioJpaController;
import formulario.FormAdministrador;
import formulario.FormMedico;
import formulario.FormPaciente;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import modelo.Administrador;
import modelo.Medico;
import modelo.Paciente;
import modelo.Usuario;

/**
 *
 * @author carlo
 */
public class PanelUsuarios extends javax.swing.JPanel {

    private UsuarioJpaController cUsuario;
    private EntityManagerFactory emf;
    private List<Usuario> usuarios;
    private DefaultListModel<Usuario> model;
    private FormAdministrador formAdministrador;
    private FormMedico formMedico;
    private FormPaciente formPaciente;

    /**
     * Creates new form PanelUsuarios
     */
    public PanelUsuarios() {
        initComponents();
        // Agrega esto al constructor de PanelUsuarios después de initComponents()
        listaUsuarios.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof Usuario) {
                    Usuario usuario = (Usuario) value;
                    String texto = usuario.getNombreUsuario() + " (" + usuario.getRol() + ")";

                    // Color diferente según el rol
                    switch (usuario.getRol()) {
                        case "administrador":
                            setForeground(Color.BLUE);
                            break;
                        case "medico":
                            setForeground(new Color(0, 128, 0)); // Verde oscuro
                            break;
                        case "paciente":
                            setForeground(Color.RED);
                            break;
                    }

                    setText(texto);
                }

                return this;
            }
        });
        emf = Persistence.createEntityManagerFactory("EstarBienPU");
        cUsuario = new UsuarioJpaController(emf);
        usuarios = cUsuario.findUsuarioEntities();
        model = new DefaultListModel<Usuario>();
        listaUsuarios.setModel(model);
        initPaneles();
        configurarComboBox();
        llenarDatos();
    }

    public void llenarDatos() {
        model.clear(); // Limpiar lista antes de llenarla
        usuarios = cUsuario.findUsuarioEntities(); // Actualizar la lista de usuarios

        for (Usuario u : usuarios) {
            // Mostrar información específica según el rol
            String infoUsuario = u.getNombreUsuario() + " (" + u.getRol() + ")";
            if (u.getAdministrador() != null) {
                infoUsuario += " - " + u.getAdministrador().getNombre();
            } else if (u.getMedico() != null) {
                infoUsuario += " - " + u.getMedico().getNombre() + " " + u.getMedico().getApellidoPaterno();
            } else if (u.getPaciente() != null) {
                infoUsuario += " - " + u.getPaciente().getNombre() + " " + u.getPaciente().getApellidoPaterno();
            }

            model.addElement(u); // Agregar el usuario con información extendida
        }
    }

    private void initPaneles() {
        formAdministrador = new FormAdministrador(this);
        formMedico = new FormMedico(this);
        formPaciente = new FormPaciente(this);

        //se agregan los paneles al panelContenedor para mostrar los formularios dependiendo de la seleccion del usuario
        panelContenedor2.add(formAdministrador);
        panelContenedor2.add(formMedico);
        panelContenedor2.add(formPaciente);

        mostrarPanel("mainPanel", formAdministrador);
    }

    private void configurarComboBox() {
        comboRol.setModel(new DefaultComboBoxModel<>(new String[]{
            "administrador", "medico", "paciente"}));

        comboRol.addActionListener(e -> cambiarFormulario());
    }

    private void cambiarFormulario() {
        String rol = (String) comboRol.getSelectedItem();

        switch (rol) {
            case "administrador":
                mostrarPanel("administrador", formAdministrador);
                break;
            case "medico":
                mostrarPanel("medico", formMedico);
                break;
            case "paciente":
                mostrarPanel("paciente", formPaciente);
                break;
        }
    }

    public void mostrarPanel(String nombrePanel, JPanel panel) {
        if (panelContenedor2.getComponentCount() > 0) {
            for (java.awt.Component comp : panelContenedor2.getComponents()) {
                if (comp.getName() != null && comp.getName().equals(nombrePanel)) {
                    CardLayout layout = (CardLayout) panelContenedor2.getLayout();
                    layout.show(panelContenedor2, nombrePanel);
                    return;
                }
            }
        }
        panel.setName(nombrePanel); // Asigna un nombre único al panel
        panelContenedor2.add(panel, nombrePanel); // Añade el panel al CardLayout
        CardLayout layout = (CardLayout) panelContenedor2.getLayout();
        layout.show(panelContenedor2, nombrePanel); // Muestra el panel correspondiente
        panelContenedor2.revalidate(); // Fuerza la actualización del contenedor
        panelContenedor2.repaint();    // Redibuja el contenedor
    }

    public void notificarCambioUsuario() {
        // Actualizar la lista cuando se agrega/edita/elimina un usuario
        usuarios = cUsuario.findUsuarioEntities();
        llenarDatos();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        listaUsuarios = new javax.swing.JList<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        datosUsuario = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        panelContenedorGeneral = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        comboRol = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        panelContenedor2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        recargarUsuarios = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        listaUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listaUsuariosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(listaUsuarios);

        add(jScrollPane2, java.awt.BorderLayout.LINE_START);

        datosUsuario.setColumns(20);
        datosUsuario.setRows(5);
        jScrollPane1.setViewportView(datosUsuario);

        add(jScrollPane1, java.awt.BorderLayout.LINE_END);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Lista de Todos Los Usuarios");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Detalle del Usuario");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setText("PANEL USUARIOS - ADMINISTRADOR");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(49, 49, 49))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(329, 329, 329)
                .addComponent(jLabel4)
                .addContainerGap(352, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)))
        );

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jLabel2.setText("Selecciona el usuario al que deseas agregar");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(238, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboRol, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 13, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)))
        );

        panelContenedor2.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout panelContenedorGeneralLayout = new javax.swing.GroupLayout(panelContenedorGeneral);
        panelContenedorGeneral.setLayout(panelContenedorGeneralLayout);
        panelContenedorGeneralLayout.setHorizontalGroup(
            panelContenedorGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelContenedor2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelContenedorGeneralLayout.setVerticalGroup(
            panelContenedorGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContenedorGeneralLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelContenedor2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(panelContenedorGeneral, java.awt.BorderLayout.CENTER);

        recargarUsuarios.setText("recargarUsuarios");
        recargarUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recargarUsuariosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(336, 336, 336)
                .addComponent(recargarUsuarios)
                .addContainerGap(655, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addComponent(recargarUsuarios)
                .addGap(32, 32, 32))
        );

        add(jPanel3, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void recargarUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recargarUsuariosActionPerformed
        // Actualizar la lista de usuarios desde la base de datos
        usuarios = cUsuario.findUsuarioEntities();
        llenarDatos();
        JOptionPane.showMessageDialog(this, "Lista de usuarios actualizada");
    }//GEN-LAST:event_recargarUsuariosActionPerformed

    private void listaUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaUsuariosMouseClicked

        Usuario usuarioSeleccionado = (Usuario) listaUsuarios.getSelectedValue();
        if (usuarioSeleccionado != null) {
            StringBuilder datos = new StringBuilder();
            datos.append("Nombre de Usuario: ").append(usuarioSeleccionado.getNombreUsuario()).append("\n")
                    .append("Rol: ").append(usuarioSeleccionado.getRol()).append("\n")
                    .append("IdUsuario: ").append(usuarioSeleccionado.getIdUsuario()).append("\n");

            // Mostrar información específica según el rol
            if (usuarioSeleccionado.getAdministrador() != null) {
                Administrador admin = usuarioSeleccionado.getAdministrador();
                datos.append("Tipo: Administrador\n")
                        .append("Nombre: ").append(admin.getNombre()).append("\n")
                        .append("Correo: ").append(admin.getCorreo()).append("\n")
                        .append("Teléfono: ").append(admin.getTelefono());
            } else if (usuarioSeleccionado.getMedico() != null) {
                Medico medico = usuarioSeleccionado.getMedico();
                datos.append("Tipo: Médico\n")
                        .append("Nombre: ").append(medico.getNombre()).append(" ")
                        .append(medico.getApellidoPaterno()).append(" ")
                        .append(medico.getApellidoMaterno()).append("\n")
                        .append("Especialidad: ").append(medico.getEspecialidad()).append("\n")
                        .append("Cédula: ").append(medico.getCedulaProfesional());
            } else if (usuarioSeleccionado.getPaciente() != null) {
                Paciente paciente = usuarioSeleccionado.getPaciente();
                datos.append("Tipo: Paciente\n")
                        .append("Nombre: ").append(paciente.getNombre()).append(" ")
                        .append(paciente.getApellidoPaterno()).append(" ")
                        .append(paciente.getApellidoMaterno()).append("\n")
                        .append("Teléfono: ").append(paciente.getTelefono()).append("\n")
                        .append("Correo: ").append(paciente.getCorreoElectronico()).append("\n")
                        .append("Genero: ").append(paciente.getGenero()).append("\n")
                        .append("Fecha de nac: ").append(paciente.getFechaNacimiento()).append("\n")
                        .append("Direccion: ").append(paciente.getDireccion()).append("\n")
                        .append("Alergias: ").append(paciente.getAlergias()).append("\n");
            }

            datosUsuario.setText(datos.toString());
        }

    }//GEN-LAST:event_listaUsuariosMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> comboRol;
    private javax.swing.JTextArea datosUsuario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<Usuario> listaUsuarios;
    private javax.swing.JPanel panelContenedor2;
    private javax.swing.JPanel panelContenedorGeneral;
    private javax.swing.JButton recargarUsuarios;
    // End of variables declaration//GEN-END:variables
}
