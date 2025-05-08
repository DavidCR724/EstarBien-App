/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package formulario;

import control.AdministradorJpaController;
import control.UsuarioJpaController;
import static java.text.NumberFormat.Field.INTEGER;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import modelo.Administrador;
import modelo.Usuario;
import vista.PanelUsuarios;

/**
 *
 * @author carlo
 */
public class FormAdministrador extends javax.swing.JPanel {

    private EntityManagerFactory emf;
    private PanelUsuarios panelPadre;

    /**
     * Creates new form formAdministrador
     */
    public FormAdministrador(PanelUsuarios panelPadre) {
        initComponents();
        emf = Persistence.createEntityManagerFactory("EstarBienPU");
        this.panelPadre = panelPadre;
        idEliminarAdministrador.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                cargarSiEsNecesario();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                cargarSiEsNecesario();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                cargarSiEsNecesario();
            }

            private void cargarSiEsNecesario() {
                if (editarBox.isSelected() && !idEliminarAdministrador.getText().isEmpty()) {
                    try {
                        int id = Integer.parseInt(idEliminarAdministrador.getText());
                        cargarDatosAdministrador(id);
                    } catch (NumberFormatException ex) {
                        // No hacer nada si no es un número válido
                    }
                }
            }
        });
    }
    // En FormAdministrador.java

    public void agregarAdministrador() {
        try {
            // Crear usuario base
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(nombreUsuario.getText());
            usuario.setContrasena(new String(contrasenaUsuario.getPassword()));
            usuario.setRol("administrador");
            usuario.setFechaRegistro(new Date());
            usuario.setActivo(true);
            // Crear administrador
            Administrador admin = new Administrador();
            admin.setNombre(nombreAdmin.getText());
            if (validarCorreo(correoAdmin.getText())) {
                admin.setCorreo(correoAdmin.getText());
            } else {
                JOptionPane.showMessageDialog(this, "Error al añadir administrador correo invalido ",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            admin.setTelefono(telefonoAdministrador.getText());
            admin.setIdUsuario(usuario);
            admin.setCreatedAt(new Date());
            

            // Persistir
            UsuarioJpaController usuarioController = new UsuarioJpaController(emf);
            AdministradorJpaController adminController = new AdministradorJpaController(emf);
            usuarioController.create(usuario);
            adminController.create(admin);

            JOptionPane.showMessageDialog(this, "Administrador registrado exitosamente!");
            limpiarCampos();
            if (panelPadre != null) {
                panelPadre.notificarCambioUsuario();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar administrador: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editarAdministrador(int idUsuario) {
        try {
            // Validar campos obligatorios
            if (nombreUsuario.getText().isEmpty() || contrasenaUsuario.getPassword().length == 0
                    || nombreAdmin.getText().isEmpty() || correoAdmin.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            UsuarioJpaController usuarioController = new UsuarioJpaController(emf);
            AdministradorJpaController adminController = new AdministradorJpaController(emf);

            // Obtener y actualizar usuario
            Usuario usuario = usuarioController.findUsuario(idUsuario);
            usuario.setNombreUsuario(nombreUsuario.getText());
            usuario.setContrasena(new String(contrasenaUsuario.getPassword()));

            // Obtener y actualizar administrador
            Administrador admin = adminController.findAdministrador(usuario.getAdministrador().getIdAdmin());
            admin.setNombre(nombreAdmin.getText());
            if (validarCorreo(correoAdmin.getText())) {
                admin.setCorreo(correoAdmin.getText());
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar administrador correo invalido ",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

            admin.setTelefono(telefonoAdministrador.getText());

            // Guardar cambios
            usuarioController.edit(usuario);
            adminController.edit(admin);

            JOptionPane.showMessageDialog(this, "Administrador actualizado exitosamente!");
            if (panelPadre != null) {
                panelPadre.notificarCambioUsuario();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar administrador: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cargarDatosAdministrador(int idUsuario) {
        try {
            UsuarioJpaController usuarioController = new UsuarioJpaController(emf);
            AdministradorJpaController adminController = new AdministradorJpaController(emf);

            // Buscar el usuario y el administrador
            Usuario usuario = usuarioController.findUsuario(idUsuario);
            Administrador admin = adminController.findAdministrador(usuario.getAdministrador().getIdAdmin());

            // Verificar que existan ambos
            if (usuario != null && admin != null) {
                // Cargar datos del usuario
                nombreUsuario.setText(usuario.getNombreUsuario());
                contrasenaUsuario.setText(usuario.getContrasena());

                // Cargar datos del administrador
                nombreAdmin.setText(admin.getNombre());
                correoAdmin.setText(admin.getCorreo());
                telefonoAdministrador.setText(admin.getTelefono());

                JOptionPane.showMessageDialog(this, "Datos cargados correctamente");
            } else {
                
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos la id no es de administrador: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        nombreAdmin.setText("");
        correoAdmin.setText("");
        telefonoAdministrador.setText("");
        contrasenaUsuario.setText("");
        nombreUsuario.setText("");
        idEliminarAdministrador.setText("");
    }

    private boolean validarCorreo(String correo) {
        return correo.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    }

    public void eliminarAdministrador(int idUsuario) {
        try {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar este administrador?", "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                UsuarioJpaController usuarioController = new UsuarioJpaController(emf);
                AdministradorJpaController adminController = new AdministradorJpaController(emf);

                Usuario usuario = usuarioController.findUsuario(idUsuario);
                adminController.destroy(usuario.getAdministrador().getIdAdmin());
                usuarioController.destroy(idUsuario);

                JOptionPane.showMessageDialog(this, "Administrador eliminado exitosamente!");
                limpiarCampos();
                if (panelPadre != null) {
                    panelPadre.notificarCambioUsuario();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar administrador: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        agregarBoton = new javax.swing.JButton();
        editarBoton = new javax.swing.JButton();
        nombreUsuario = new javax.swing.JTextField();
        contrasenaUsuario = new javax.swing.JPasswordField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        correoAdmin = new javax.swing.JTextField();
        nombreAdmin = new javax.swing.JTextField();
        telefonoAdministrador = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        eliminarBox = new javax.swing.JCheckBox();
        agregarBox = new javax.swing.JCheckBox();
        jLabel12 = new javax.swing.JLabel();
        idEliminarAdministrador = new javax.swing.JTextField();
        eliminarBoton = new javax.swing.JButton();
        editarBox = new javax.swing.JCheckBox();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("ADMINISTRADOR");

        agregarBoton.setBackground(new java.awt.Color(0, 204, 0));
        agregarBoton.setForeground(new java.awt.Color(255, 255, 255));
        agregarBoton.setText("Agregar");
        agregarBoton.setBorderPainted(false);
        agregarBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarBotonActionPerformed(evt);
            }
        });

        editarBoton.setBackground(new java.awt.Color(0, 0, 255));
        editarBoton.setForeground(new java.awt.Color(255, 255, 255));
        editarBoton.setText("Editar");
        editarBoton.setBorderPainted(false);
        editarBoton.setEnabled(false);
        editarBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarBotonActionPerformed(evt);
            }
        });

        nombreUsuario.setText("Nombre de usuario");

        contrasenaUsuario.setText("contrasena");

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("Adminitrador");
        jScrollPane2.setViewportView(jTextArea1);

        correoAdmin.setText("ejemplo@gmail.com");

        nombreAdmin.setText("Nombre del Administrador");
        nombreAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombreAdminActionPerformed(evt);
            }
        });

        telefonoAdministrador.setText("telefono");

        jLabel2.setText("Nombre del Usuario:");

        jLabel3.setText("Contraseña del Usuario:");

        jLabel4.setText("Rol del Usuario:");

        jLabel5.setText("Nombre(s):");

        jLabel6.setText("Correo electronico:");

        jLabel13.setText("Telefono");

        buttonGroup1.add(eliminarBox);
        eliminarBox.setText("Eliminar");
        eliminarBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarBoxActionPerformed(evt);
            }
        });

        buttonGroup1.add(agregarBox);
        agregarBox.setSelected(true);
        agregarBox.setText("Agregar");
        agregarBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarBoxActionPerformed(evt);
            }
        });

        jLabel12.setText("Ingrese el ID a eliminar");

        idEliminarAdministrador.setText("ID");
        idEliminarAdministrador.setEnabled(false);

        eliminarBoton.setBackground(new java.awt.Color(255, 0, 0));
        eliminarBoton.setForeground(new java.awt.Color(255, 255, 255));
        eliminarBoton.setText("Eliminar");
        eliminarBoton.setBorderPainted(false);
        eliminarBoton.setEnabled(false);
        eliminarBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarBotonActionPerformed(evt);
            }
        });

        buttonGroup1.add(editarBox);
        editarBox.setText("Editar");
        editarBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(nombreUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                                .addComponent(contrasenaUsuario))
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(69, 69, 69)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel13)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(agregarBoton)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(editarBoton))
                                .addComponent(correoAdmin, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(nombreAdmin, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                                .addComponent(telefonoAdministrador, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(98, 98, 98)
                        .addComponent(agregarBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(editarBox)
                        .addGap(84, 84, 84)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel12)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(idEliminarAdministrador, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(eliminarBoton))
                            .addGap(46, 46, 46)))
                    .addComponent(eliminarBox))
                .addGap(40, 40, 40))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nombreUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nombreAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(eliminarBox)
                        .addComponent(agregarBox)
                        .addComponent(editarBox)
                        .addComponent(jLabel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(idEliminarAdministrador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(eliminarBoton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contrasenaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(correoAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel13))
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(telefonoAdministrador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(editarBoton)
                            .addComponent(agregarBoton)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(113, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void nombreAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nombreAdminActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nombreAdminActionPerformed

    private void eliminarBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarBoxActionPerformed
        nombreUsuario.setEnabled(false);
        contrasenaUsuario.setEnabled(false);
        agregarBoton.setEnabled(false);
        editarBoton.setEnabled(false);
        nombreAdmin.setEnabled(false);
        telefonoAdministrador.setEnabled(false);
        correoAdmin.setEnabled(false);
        jTextArea1.setEnabled(false);

        idEliminarAdministrador.setEnabled(true);
        eliminarBoton.setEnabled(true);
    }//GEN-LAST:event_eliminarBoxActionPerformed

    private void agregarBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarBoxActionPerformed
        idEliminarAdministrador.setEnabled(false);
        eliminarBoton.setEnabled(false);

        nombreUsuario.setEnabled(true);
        contrasenaUsuario.setEnabled(true);
        agregarBoton.setEnabled(true);
        editarBoton.setEnabled(false);
        nombreAdmin.setEnabled(true);
        telefonoAdministrador.setEnabled(true);
        correoAdmin.setEnabled(true);

    }//GEN-LAST:event_agregarBoxActionPerformed

    private void editarBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarBotonActionPerformed
        try {
            int id = Integer.parseInt(idEliminarAdministrador.getText());
            editarAdministrador(id);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID válido",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_editarBotonActionPerformed

    private void editarBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarBoxActionPerformed
        idEliminarAdministrador.setEnabled(true);
        eliminarBoton.setEnabled(false);

        nombreUsuario.setEnabled(true);
        contrasenaUsuario.setEnabled(true);
        agregarBoton.setEnabled(false);
        editarBoton.setEnabled(true);
        nombreAdmin.setEnabled(true);
        telefonoAdministrador.setEnabled(true);
        correoAdmin.setEnabled(true);

        // Si ya hay un ID ingresado, cargar los datos automáticamente
        if (!idEliminarAdministrador.getText().isEmpty() && !idEliminarAdministrador.getText().equals("ID a eliminar")) {
            try {
                int id = Integer.parseInt(idEliminarAdministrador.getText());
                cargarDatosAdministrador(id);
            } catch (NumberFormatException ex) {
                }
        }


    }//GEN-LAST:event_editarBoxActionPerformed

    private void agregarBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarBotonActionPerformed
        agregarAdministrador();
    }//GEN-LAST:event_agregarBotonActionPerformed

    private void eliminarBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarBotonActionPerformed
        int id = Integer.parseInt(idEliminarAdministrador.getText());
        eliminarAdministrador(id);
    }//GEN-LAST:event_eliminarBotonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agregarBoton;
    private javax.swing.JCheckBox agregarBox;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPasswordField contrasenaUsuario;
    private javax.swing.JTextField correoAdmin;
    private javax.swing.JButton editarBoton;
    private javax.swing.JCheckBox editarBox;
    private javax.swing.JButton eliminarBoton;
    private javax.swing.JCheckBox eliminarBox;
    private javax.swing.JTextField idEliminarAdministrador;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField nombreAdmin;
    private javax.swing.JTextField nombreUsuario;
    private javax.swing.JTextField telefonoAdministrador;
    // End of variables declaration//GEN-END:variables

}
