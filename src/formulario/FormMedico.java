/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package formulario;

import control.MedicoJpaController;
import control.UsuarioJpaController;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import modelo.Medico;
import modelo.Usuario;
import vista.PanelUsuarios;

/**
 *
 * @author carlo
 */
public class FormMedico extends javax.swing.JPanel {
    private EntityManagerFactory emf;
    private PanelUsuarios panelPadre;

    /**
     * Creates new form formMedico
     */
    public FormMedico(PanelUsuarios panelPadre) {
        initComponents();
        emf = Persistence.createEntityManagerFactory("EstarBienPU");
        this.panelPadre = panelPadre;
        idEliminarMedico.getDocument().addDocumentListener(new DocumentListener() {
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
                if (editarBox.isSelected() && !idEliminarMedico.getText().isEmpty()) {
                    try {
                        int id = Integer.parseInt(idEliminarMedico.getText());
                        cargarDatosMedico(id);
                    } catch (NumberFormatException ex) {
                        // No hacer nada si no es un número válido
                    }
                }
            }
        });
    }
    public void agregarMedico() {
        try {
            // Crear usuario base
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(nombreUsuario.getText());
            usuario.setContrasena(new String(contrasenaUsuario.getPassword()));
            usuario.setRol("medico");
            usuario.setFechaRegistro(new Date());
            usuario.setActivo(true);
            // Crear administrador
            Medico medico = new Medico();
            medico.setNombre(nombreMedico.getText());
            medico.setApellidoPaterno(apellidoPMedico.getText());
            medico.setApellidoMaterno(apellidoMMedico.getText());
            if (validarCorreo(correoMedico.getText())) {
                medico.setCorreoElectronico(correoMedico.getText());
            } else {
                JOptionPane.showMessageDialog(this, "Error al añadir administrador correo invalido ",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            medico.setTelefono(telefonoMedico.getText());
            medico.setIdUsuario(usuario);
            medico.setEspecialidad(especialidadMedico.getText());
            medico.setCedulaProfesional(cedulaMedico.getText());
            medico.setCreatedAt(new Date());
            medico.setUpdatedAt(new Date());
            medico.setActivo(Boolean.TRUE);
            

            // Persistir
            UsuarioJpaController usuarioController = new UsuarioJpaController(emf);
            MedicoJpaController medicoController = new MedicoJpaController(emf);
            usuarioController.create(usuario);
            medicoController.create(medico);

            JOptionPane.showMessageDialog(this, "Medico registrado exitosamente!");
            limpiarCampos();
            if (panelPadre != null) {
                panelPadre.notificarCambioUsuario();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar Medico: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void editarMedico(int idUsuario) {
        try {
            // Validar campos obligatorios
            if (nombreUsuario.getText().isEmpty() || contrasenaUsuario.getPassword().length == 0
                    || nombreMedico.getText().isEmpty() || correoMedico.getText().isEmpty() || apellidoMMedico.getText().isEmpty()
                    || apellidoPMedico.getText().isEmpty() || cedulaMedico.getText().isEmpty() ||
                    especialidadMedico.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            UsuarioJpaController usuarioController = new UsuarioJpaController(emf);
            MedicoJpaController medicoController = new MedicoJpaController(emf);

            // Obtener y actualizar usuario
            Usuario usuario = usuarioController.findUsuario(idUsuario);
            usuario.setNombreUsuario(nombreUsuario.getText());
            usuario.setContrasena(new String(contrasenaUsuario.getPassword()));

            // Obtener y actualizar administrador
            Medico medico = medicoController.findMedico(usuario.getMedico().getIdMedico());
            medico.setNombre(nombreMedico.getText());
            medico.setApellidoPaterno(apellidoPMedico.getText()); 
            medico.setApellidoMaterno(apellidoMMedico.getText());
            medico.setEspecialidad(especialidadMedico.getText());
            medico.setCedulaProfesional(cedulaMedico.getText());
            
            if (validarCorreo(correoMedico.getText())) {
                medico.setCorreoElectronico(correoMedico.getText());
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar medico correo invalido ",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

            medico.setTelefono(telefonoMedico.getText());
            medico.setUpdatedAt(new Date());
            

            // Guardar cambios
            usuarioController.edit(usuario);
            medicoController.edit(medico);

            JOptionPane.showMessageDialog(this, "Medico actualizado exitosamente!");
            if (panelPadre != null) {
                panelPadre.notificarCambioUsuario();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar administrador: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void eliminarMedico(int idUsuario) {
        try {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar este medico?", "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                UsuarioJpaController usuarioController = new UsuarioJpaController(emf);
                MedicoJpaController medicoController = new MedicoJpaController(emf);

                Usuario usuario = usuarioController.findUsuario(idUsuario);
                medicoController.destroy(usuario.getMedico().getIdMedico());
                usuarioController.destroy(idUsuario);

                JOptionPane.showMessageDialog(this, "Medico eliminado exitosamente!");
                limpiarCampos();
                if (panelPadre != null) {
                    panelPadre.notificarCambioUsuario();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar al medico: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cargarDatosMedico(int idUsuario) {
        try {
            UsuarioJpaController usuarioController = new UsuarioJpaController(emf);
            MedicoJpaController medicoController = new MedicoJpaController(emf);

            // Buscar el usuario y el administrador
            Usuario usuario = usuarioController.findUsuario(idUsuario);
            Medico medico = medicoController.findMedico(usuario.getMedico().getIdMedico());

            // Verificar que existan ambos
            if (usuario != null && medico != null) {
                // Cargar datos del usuario
                nombreUsuario.setText(usuario.getNombreUsuario());
                contrasenaUsuario.setText(usuario.getContrasena());

                // Cargar datos del medico
                nombreMedico.setText(medico.getNombre());
                apellidoPMedico.setText(medico.getApellidoPaterno());
                apellidoMMedico.setText(medico.getApellidoMaterno());
                especialidadMedico.setText(medico.getEspecialidad());
                cedulaMedico.setText(medico.getCedulaProfesional());
                correoMedico.setText(medico.getCorreoElectronico());
                telefonoMedico.setText(medico.getTelefono());

                JOptionPane.showMessageDialog(this, "Datos cargados correctamente");
            } else {
                
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos la id no es de medico: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private boolean validarCorreo(String correo) {
        return correo.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    }
    private void limpiarCampos() {
        nombreMedico.setText("");
        correoMedico.setText("");
        telefonoMedico.setText("");
        apellidoMMedico.setText("");
        apellidoPMedico.setText("");
        cedulaMedico.setText("");
        especialidadMedico.setText("");
        
        contrasenaUsuario.setText("");
        nombreUsuario.setText("");
        idEliminarMedico.setText("ID");
    }
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        rolUsuario = new javax.swing.JTextArea();
        agregarBoton = new javax.swing.JButton();
        correoMedico = new javax.swing.JTextField();
        eliminarBoton = new javax.swing.JButton();
        editarBoton = new javax.swing.JButton();
        nombreUsuario = new javax.swing.JTextField();
        nombreMedico = new javax.swing.JTextField();
        contrasenaUsuario = new javax.swing.JPasswordField();
        telefonoMedico = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        apellidoPMedico = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        apellidoMMedico = new javax.swing.JTextField();
        especialidadMedico = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cedulaMedico = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        agregarBox = new javax.swing.JCheckBox();
        eliminarBox = new javax.swing.JCheckBox();
        jLabel12 = new javax.swing.JLabel();
        idEliminarMedico = new javax.swing.JTextField();
        editarBox = new javax.swing.JCheckBox();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Medico");

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        rolUsuario.setEditable(false);
        rolUsuario.setColumns(20);
        rolUsuario.setRows(5);
        rolUsuario.setText("Medico");
        jScrollPane2.setViewportView(rolUsuario);

        agregarBoton.setBackground(new java.awt.Color(0, 204, 0));
        agregarBoton.setForeground(new java.awt.Color(255, 255, 255));
        agregarBoton.setText("Agregar");
        agregarBoton.setBorderPainted(false);
        agregarBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarBotonActionPerformed(evt);
            }
        });

        correoMedico.setText("ejemplo@gmail.com");

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

        nombreMedico.setText("Nombre del Medico");
        nombreMedico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombreMedicoActionPerformed(evt);
            }
        });

        contrasenaUsuario.setText("contrasena");

        telefonoMedico.setText("Telefono");

        jLabel2.setText("Nombre del Usuario:");

        jLabel3.setText("Contraeña del Usuario:");

        jLabel4.setText("Rol del Usuario:");

        jLabel5.setText("Nombre(s)");

        jLabel6.setText("Apellido Paterno");

        jLabel7.setText("Especialidad");

        apellidoPMedico.setText("Apellido Paterno");

        jLabel8.setText("Apellido Materno");

        apellidoMMedico.setText("Apellido Materno");

        especialidadMedico.setText("Especialidad");

        jLabel9.setText("Cedula");

        cedulaMedico.setText("Cedula");

        jLabel10.setText("Telefono");

        jLabel11.setText("Correo Electronico");

        buttonGroup1.add(agregarBox);
        agregarBox.setSelected(true);
        agregarBox.setText("Agregar");
        agregarBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarBoxActionPerformed(evt);
            }
        });

        buttonGroup1.add(eliminarBox);
        eliminarBox.setText("Eliminar");
        eliminarBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarBoxActionPerformed(evt);
            }
        });

        jLabel12.setText("Ingrese el ID a eliminar");

        idEliminarMedico.setText("ID");
        idEliminarMedico.setEnabled(false);

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
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(nombreUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(contrasenaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6)
                            .addComponent(nombreMedico, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                            .addComponent(jLabel5)
                            .addComponent(apellidoPMedico)
                            .addComponent(jLabel8)
                            .addComponent(apellidoMMedico)
                            .addComponent(jLabel7)
                            .addComponent(especialidadMedico))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(telefonoMedico, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(correoMedico, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(agregarBoton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(editarBoton))
                            .addComponent(cedulaMedico, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 205, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(242, 242, 242)
                        .addComponent(agregarBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(editarBox)
                        .addGap(58, 58, 58)))
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(eliminarBox)
                    .addComponent(jLabel12)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(idEliminarMedico, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(eliminarBoton)))
                .addGap(8, 8, 8))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eliminarBox)
                    .addComponent(agregarBox)
                    .addComponent(editarBox)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5)
                    .addComponent(jLabel9)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombreUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nombreMedico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cedulaMedico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idEliminarMedico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eliminarBoton))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel3)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(apellidoPMedico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contrasenaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(telefonoMedico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel4)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(apellidoMMedico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(correoMedico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(especialidadMedico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(agregarBoton)
                    .addComponent(editarBoton))
                .addContainerGap(126, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void nombreMedicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nombreMedicoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nombreMedicoActionPerformed

    private void agregarBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarBoxActionPerformed
        idEliminarMedico.setEnabled(false);
        eliminarBoton.setEnabled(false);
        
        nombreUsuario.setEnabled(true);
        contrasenaUsuario.setEnabled(true);
        rolUsuario.setEnabled(true);
        nombreMedico.setEnabled(true);
        apellidoPMedico.setEnabled(true);
        apellidoMMedico.setEnabled(true);
        especialidadMedico.setEnabled(true);
        cedulaMedico.setEnabled(true);
        telefonoMedico.setEnabled(true);
        correoMedico.setEnabled(true);
        agregarBoton.setEnabled(true);
        editarBoton.setEnabled(false);
    }//GEN-LAST:event_agregarBoxActionPerformed

    private void eliminarBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarBoxActionPerformed
        nombreUsuario.setEnabled(false);
        contrasenaUsuario.setEnabled(false);
        rolUsuario.setEnabled(false);
        nombreMedico.setEnabled(false);
        apellidoPMedico.setEnabled(false);
        apellidoMMedico.setEnabled(false);
        especialidadMedico.setEnabled(false);
        cedulaMedico.setEnabled(false);
        telefonoMedico.setEnabled(false);
        correoMedico.setEnabled(false);
        agregarBoton.setEnabled(false);
        editarBoton.setEnabled(false);
        
        idEliminarMedico.setEnabled(true);
        eliminarBoton.setEnabled(true);
    }//GEN-LAST:event_eliminarBoxActionPerformed

    private void editarBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarBotonActionPerformed
        try{
            int id = Integer.parseInt(idEliminarMedico.getText());
            editarMedico(id);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID válido",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_editarBotonActionPerformed

    private void editarBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarBoxActionPerformed
        idEliminarMedico.setEnabled(true);
        eliminarBoton.setEnabled(false);
        
        nombreUsuario.setEnabled(true);
        contrasenaUsuario.setEnabled(true);
        rolUsuario.setEnabled(true);
        nombreMedico.setEnabled(true);
        apellidoPMedico.setEnabled(true);
        apellidoMMedico.setEnabled(true);
        especialidadMedico.setEnabled(true);
        cedulaMedico.setEnabled(true);
        telefonoMedico.setEnabled(true);
        correoMedico.setEnabled(true);
        agregarBoton.setEnabled(false);
        editarBoton.setEnabled(true);
        
        // Si ya hay un ID ingresado, cargar los datos automáticamente
        if (!idEliminarMedico.getText().isEmpty() && !idEliminarMedico.getText().equals("ID a eliminar")) {
            try {
                int id = Integer.parseInt(idEliminarMedico.getText());
                cargarDatosMedico(id);
            } catch (NumberFormatException ex) {
                }
        }
    }//GEN-LAST:event_editarBoxActionPerformed

    private void agregarBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarBotonActionPerformed
       agregarMedico();
    }//GEN-LAST:event_agregarBotonActionPerformed

    private void eliminarBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarBotonActionPerformed
        try{
            int id = Integer.parseInt(idEliminarMedico.getText());
            eliminarMedico(id);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID válido",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_eliminarBotonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agregarBoton;
    private javax.swing.JCheckBox agregarBox;
    private javax.swing.JTextField apellidoMMedico;
    private javax.swing.JTextField apellidoPMedico;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField cedulaMedico;
    private javax.swing.JPasswordField contrasenaUsuario;
    private javax.swing.JTextField correoMedico;
    private javax.swing.JButton editarBoton;
    private javax.swing.JCheckBox editarBox;
    private javax.swing.JButton eliminarBoton;
    private javax.swing.JCheckBox eliminarBox;
    private javax.swing.JTextField especialidadMedico;
    private javax.swing.JTextField idEliminarMedico;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField nombreMedico;
    private javax.swing.JTextField nombreUsuario;
    private javax.swing.JTextArea rolUsuario;
    private javax.swing.JTextField telefonoMedico;
    // End of variables declaration//GEN-END:variables
}
