/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package formulario;

import control.PacienteJpaController;
import control.UsuarioJpaController;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import modelo.Paciente;
import modelo.Usuario;
import vista.PanelUsuarios;

/**
 *
 * @author carlo
 */
public class FormPaciente extends javax.swing.JPanel {
    private EntityManagerFactory emf;
    private PanelUsuarios panelPadre;
    /**
     * Creates new form formPaciente
     */
    public FormPaciente(PanelUsuarios panelUsuarios) {
        initComponents();
        establecerModeloFechaNacimiento();
        emf = Persistence.createEntityManagerFactory("EstarBienPU");
        this.panelPadre = panelPadre;
        idEliminarPaciente.getDocument().addDocumentListener(new DocumentListener() {
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
                if (editarBox.isSelected() && !idEliminarPaciente.getText().isEmpty()) {
                    try {
                        int id = Integer.parseInt(idEliminarPaciente.getText());
                        cargarDatosPaciente(id);
                    } catch (NumberFormatException ex) {
                        // No hacer nada si no es un número válido
                    }
                }
            }
        });
    }
    
    public void agregarPaciente() {
        try {
            // Crear usuario base
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(nombreUsuario.getText());
            usuario.setContrasena(new String(contrasenaUsuario.getPassword()));
            usuario.setRol("paciente");
            usuario.setFechaRegistro(new Date());
            usuario.setActivo(true);
            // Crear administrador
            Paciente paciente = new Paciente();
            paciente.setNombre(nombrePaciente.getText());
            paciente.setApellidoPaterno(apellidoPPaciente.getText());
            paciente.setApellidoMaterno(apellidoMPaciente.getText());
            paciente.setDireccion(direccionPaciente.getText());
            paciente.setAlergias(alergiaPaciente.getText());
            String genero = (mascBox.isSelected()) ? "Masculino" : "Femenino";
            paciente.setGenero(genero);
            
            paciente.setFechaNacimiento((Date)fechaNac.getValue());
            if (validarCorreo(correoPaciente.getText())) {
                paciente.setCorreoElectronico(correoPaciente.getText());
            } else {
                JOptionPane.showMessageDialog(this, "Error al añadir paciente correo invalido ",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            paciente.setTelefono(telefonoPaciente.getText());
            
            paciente.setIdUsuario(usuario);
            paciente.setCreatedAt(new Date());
            paciente.setUpdatedAt(new Date());
            

            // Persistir
            UsuarioJpaController usuarioController = new UsuarioJpaController(emf);
            PacienteJpaController pacienteController = new PacienteJpaController(emf);
            usuarioController.create(usuario);
            pacienteController.create(paciente);

            JOptionPane.showMessageDialog(this, "Paciente registrado exitosamente!");
            limpiarCampos();
            if (panelPadre != null) {
                panelPadre.notificarCambioUsuario();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar paciente: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void editarPaciente(int idUsuario){
        try {
            // Validar campos obligatorios
            if (nombreUsuario.getText().isEmpty() || contrasenaUsuario.getPassword().length == 0
                    || nombrePaciente.getText().isEmpty() || correoPaciente.getText().isEmpty() || apellidoPPaciente.getText().isEmpty()
                    || apellidoMPaciente.getText().isEmpty() || direccionPaciente.getText().isEmpty() ||
                    alergiaPaciente.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            UsuarioJpaController usuarioController = new UsuarioJpaController(emf);
            PacienteJpaController pacienteController = new PacienteJpaController(emf);

            // Obtener y actualizar usuario
            Usuario usuario = usuarioController.findUsuario(idUsuario);
            usuario.setNombreUsuario(nombreUsuario.getText());
            usuario.setContrasena(new String(contrasenaUsuario.getPassword()));

            // Obtener y actualizar administrador
            Paciente paciente = pacienteController.findPaciente(usuario.getPaciente().getIdPaciente());
            paciente.setNombre(nombrePaciente.getText());
            paciente.setApellidoPaterno(apellidoPPaciente.getText()); 
            paciente.setApellidoMaterno(apellidoMPaciente.getText());
            paciente.setDireccion(direccionPaciente.getText());
            paciente.setAlergias(alergiaPaciente.getText());
            if (validarCorreo(correoPaciente.getText())) {
                paciente.setCorreoElectronico(correoPaciente.getText());
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el paciente correo invalido ",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            paciente.setTelefono(telefonoPaciente.getText());
            String genero = (mascBox.isSelected()) ? "Masculino" : "Femenino";
            paciente.setGenero(genero);
            paciente.setFechaNacimiento((Date) fechaNac.getValue());
            paciente.setUpdatedAt(new Date());
            

            // Guardar cambios
            usuarioController.edit(usuario);
            pacienteController.edit(paciente);

            JOptionPane.showMessageDialog(this, "Paciente actualizado exitosamente!");
            if (panelPadre != null) {
                panelPadre.notificarCambioUsuario();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar paciente: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void eliminarPaciente(int idUsuario) {
        try {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar este paciente?", "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                UsuarioJpaController usuarioController = new UsuarioJpaController(emf);
                PacienteJpaController pacienteController = new PacienteJpaController(emf);

                Usuario usuario = usuarioController.findUsuario(idUsuario);
                pacienteController.destroy(usuario.getPaciente().getIdPaciente());
                usuarioController.destroy(idUsuario);

                JOptionPane.showMessageDialog(this, "Paciente eliminado exitosamente!");
                limpiarCampos();
                if (panelPadre != null) {
                    panelPadre.notificarCambioUsuario();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar al paciente: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void cargarDatosPaciente(int idUsuario) {
        try {
            UsuarioJpaController usuarioController = new UsuarioJpaController(emf);
            PacienteJpaController pacienteController = new PacienteJpaController(emf);

            // Buscar el usuario y el administrador
            Usuario usuario = usuarioController.findUsuario(idUsuario);
            Paciente paciente = pacienteController.findPaciente(usuario.getPaciente().getIdPaciente());

            // Verificar que existan ambos
            if (usuario != null && paciente != null) {
                // Cargar datos del usuario
                nombreUsuario.setText(usuario.getNombreUsuario());
                contrasenaUsuario.setText(usuario.getContrasena());

                // Cargar datos del paciente
                nombrePaciente.setText(paciente.getNombre());
                apellidoPPaciente.setText(paciente.getApellidoPaterno());
                apellidoMPaciente.setText(paciente.getApellidoMaterno());
                direccionPaciente.setText(paciente.getDireccion());
                alergiaPaciente.setText(paciente.getAlergias());
                correoPaciente.setText(paciente.getCorreoElectronico());
                telefonoPaciente.setText(paciente.getTelefono());
                if(paciente.getGenero() == "Masculino"){
                    mascBox.setSelected(true);
                    femBox.setSelected(false);
                }else{
                    mascBox.setSelected(false);
                    femBox.setSelected(true);
                }
                fechaNac.setValue(paciente.getFechaNacimiento());

                JOptionPane.showMessageDialog(this, "Datos cargados correctamente");
            } else {
                
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos la id no es de paciente: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void establecerModeloFechaNacimiento() {
        // 1. Crea un SpinnerDateModel.
        SpinnerDateModel modeloFecha = new SpinnerDateModel();

        // 2. Crea un SimpleDateFormat con el formato deseado.
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

        // 3. Obtén el editor del spinner fechaNac y establece el formato.
        JSpinner.DateEditor editor = new JSpinner.DateEditor(fechaNac, formatoFecha.toPattern());
        fechaNac.setEditor(editor);

        // 4. Establece el modelo en el spinner fechaNac.
        fechaNac.setModel(modeloFecha);
    }
    private boolean validarCorreo(String correo) {
        return correo.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    }
    private void limpiarCampos() {
        nombrePaciente.setText("");
        correoPaciente.setText("");
        telefonoPaciente.setText("");
        apellidoPPaciente.setText("");
        apellidoMPaciente.setText("");
        direccionPaciente.setText("");
        alergiaPaciente.setText("");
        mascBox.setSelected(true);
        femBox.setSelected(false);
        fechaNac.setValue(new Date(2000,01,01));
        
        contrasenaUsuario.setText("");
        nombreUsuario.setText("");
        idEliminarPaciente.setText("ID");
    }
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        genero = new javax.swing.ButtonGroup();
        apellidoPPaciente = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        apellidoMPaciente = new javax.swing.JTextField();
        direccionPaciente = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        alergiaPaciente = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        rolUsuario = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        agregarBoton = new javax.swing.JButton();
        agregarBox = new javax.swing.JCheckBox();
        correoPaciente = new javax.swing.JTextField();
        eliminarBox = new javax.swing.JCheckBox();
        eliminarBoton = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        editarBoton = new javax.swing.JButton();
        idEliminarPaciente = new javax.swing.JTextField();
        nombreUsuario = new javax.swing.JTextField();
        nombrePaciente = new javax.swing.JTextField();
        editarBox = new javax.swing.JCheckBox();
        contrasenaUsuario = new javax.swing.JPasswordField();
        telefonoPaciente = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        mascBox = new javax.swing.JCheckBox();
        femBox = new javax.swing.JCheckBox();
        fechaNac = new javax.swing.JSpinner();
        jLabel13 = new javax.swing.JLabel();

        apellidoPPaciente.setText("Apellido Paterno");

        jLabel8.setText("Apellido Materno");

        apellidoMPaciente.setText("Apellido Materno");

        direccionPaciente.setText("calle # colonia, municipio, estado");

        jLabel9.setText("Alergias");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Paciente");

        alergiaPaciente.setText("Alergias");

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        rolUsuario.setEditable(false);
        rolUsuario.setColumns(20);
        rolUsuario.setRows(5);
        rolUsuario.setText("Paciente");
        jScrollPane2.setViewportView(rolUsuario);

        jLabel10.setText("Telefono");

        jLabel11.setText("Correo Electronico");

        agregarBoton.setBackground(new java.awt.Color(0, 204, 0));
        agregarBoton.setForeground(new java.awt.Color(255, 255, 255));
        agregarBoton.setText("Agregar");
        agregarBoton.setBorderPainted(false);
        agregarBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarBotonActionPerformed(evt);
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

        correoPaciente.setText("ejemplo@gmail.com");

        buttonGroup1.add(eliminarBox);
        eliminarBox.setText("Eliminar");
        eliminarBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarBoxActionPerformed(evt);
            }
        });

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

        jLabel12.setText("Ingrese el ID a eliminar");

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

        idEliminarPaciente.setText("ID");
        idEliminarPaciente.setEnabled(false);

        nombreUsuario.setText("Nombre de usuario");

        nombrePaciente.setText("Nombre del Paciente");
        nombrePaciente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombrePacienteActionPerformed(evt);
            }
        });

        buttonGroup1.add(editarBox);
        editarBox.setText("Editar");
        editarBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarBoxActionPerformed(evt);
            }
        });

        contrasenaUsuario.setText("contrasena");

        telefonoPaciente.setText("Telefono");

        jLabel2.setText("Nombre del Usuario:");

        jLabel3.setText("Contraeña del Usuario:");

        jLabel4.setText("Rol del Usuario:");

        jLabel5.setText("Nombre(s)");

        jLabel6.setText("Apellido Paterno");

        jLabel7.setText("Dirección");

        genero.add(mascBox);
        mascBox.setText("Masculino");

        genero.add(femBox);
        femBox.setText("Femenino");

        fechaNac.setModel(new javax.swing.SpinnerDateModel());

        jLabel13.setText("Fecha de nacimiento");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(242, 242, 242)
                        .addComponent(agregarBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(editarBox)
                        .addGap(60, 60, 60)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(eliminarBox)
                            .addComponent(jLabel12)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(idEliminarPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(eliminarBoton)))
                        .addGap(8, 8, 8))
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
                            .addComponent(nombrePaciente)
                            .addComponent(jLabel5)
                            .addComponent(apellidoPPaciente)
                            .addComponent(jLabel8)
                            .addComponent(apellidoMPaciente)
                            .addComponent(jLabel7)
                            .addComponent(direccionPaciente))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10)
                            .addComponent(telefonoPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(alergiaPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(correoPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(mascBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(femBox))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(agregarBoton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(editarBoton)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fechaNac, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addContainerGap(151, Short.MAX_VALUE))))
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
                    .addComponent(nombrePaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alergiaPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idEliminarPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eliminarBoton))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel3)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(apellidoPPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contrasenaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(telefonoPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel4)
                    .addComponent(jLabel11)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(apellidoMPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(correoPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fechaNac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(direccionPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mascBox)
                    .addComponent(femBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editarBoton)
                    .addComponent(agregarBoton))
                .addContainerGap(48, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void agregarBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarBoxActionPerformed
        idEliminarPaciente.setEnabled(false);
        eliminarBoton.setEnabled(false);

        nombreUsuario.setEnabled(true);
        contrasenaUsuario.setEnabled(true);
        rolUsuario.setEnabled(true);
        nombrePaciente.setEnabled(true);
        apellidoPPaciente.setEnabled(true);
        apellidoMPaciente.setEnabled(true);
        direccionPaciente.setEnabled(true);
        alergiaPaciente.setEnabled(true);
        telefonoPaciente.setEnabled(true);
        correoPaciente.setEnabled(true);
        agregarBoton.setEnabled(true);
        editarBoton.setEnabled(false);
        mascBox.setEnabled(true);
        femBox.setEnabled(true);
        fechaNac.setEnabled(true);
    }//GEN-LAST:event_agregarBoxActionPerformed

    private void eliminarBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarBoxActionPerformed
        nombreUsuario.setEnabled(false);
        contrasenaUsuario.setEnabled(false);
        rolUsuario.setEnabled(false);
        nombrePaciente.setEnabled(false);
        apellidoPPaciente.setEnabled(false);
        apellidoMPaciente.setEnabled(false);
        direccionPaciente.setEnabled(false);
        alergiaPaciente.setEnabled(false);
        telefonoPaciente.setEnabled(false);
        correoPaciente.setEnabled(false);
        agregarBoton.setEnabled(false);
        editarBoton.setEnabled(false);
        mascBox.setEnabled(false);
        femBox.setEnabled(false);
        fechaNac.setEnabled(false);

        idEliminarPaciente.setEnabled(true);
        eliminarBoton.setEnabled(true);
    }//GEN-LAST:event_eliminarBoxActionPerformed

    private void editarBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarBotonActionPerformed
        try{
            int id = Integer.parseInt(idEliminarPaciente.getText());
            editarPaciente(id);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID válido",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_editarBotonActionPerformed

    private void nombrePacienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nombrePacienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nombrePacienteActionPerformed

    private void editarBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarBoxActionPerformed
        idEliminarPaciente.setEnabled(true);
        eliminarBoton.setEnabled(false);

        nombreUsuario.setEnabled(true);
        contrasenaUsuario.setEnabled(true);
        rolUsuario.setEnabled(true);
        nombrePaciente.setEnabled(true);
        apellidoPPaciente.setEnabled(true);
        apellidoMPaciente.setEnabled(true);
        direccionPaciente.setEnabled(true);
        alergiaPaciente.setEnabled(true);
        telefonoPaciente.setEnabled(true);
        correoPaciente.setEnabled(true);
        agregarBoton.setEnabled(false);
        editarBoton.setEnabled(true);
        mascBox.setEnabled(true);
        femBox.setEnabled(true);
        fechaNac.setEnabled(true);
        
        if (!idEliminarPaciente.getText().isEmpty() && !idEliminarPaciente.getText().equals("ID a eliminar")) {
            try {
                int id = Integer.parseInt(idEliminarPaciente.getText());
                cargarDatosPaciente(id);
            } catch (NumberFormatException ex) {
              }
        }
    }//GEN-LAST:event_editarBoxActionPerformed

    private void agregarBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarBotonActionPerformed
        agregarPaciente();
    }//GEN-LAST:event_agregarBotonActionPerformed

    private void eliminarBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarBotonActionPerformed
        try{
            int id = Integer.parseInt(idEliminarPaciente.getText());
            eliminarPaciente(id);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID válido",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_eliminarBotonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agregarBoton;
    private javax.swing.JCheckBox agregarBox;
    private javax.swing.JTextField alergiaPaciente;
    private javax.swing.JTextField apellidoMPaciente;
    private javax.swing.JTextField apellidoPPaciente;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPasswordField contrasenaUsuario;
    private javax.swing.JTextField correoPaciente;
    private javax.swing.JTextField direccionPaciente;
    private javax.swing.JButton editarBoton;
    private javax.swing.JCheckBox editarBox;
    private javax.swing.JButton eliminarBoton;
    private javax.swing.JCheckBox eliminarBox;
    private javax.swing.JSpinner fechaNac;
    private javax.swing.JCheckBox femBox;
    private javax.swing.ButtonGroup genero;
    private javax.swing.JTextField idEliminarPaciente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JCheckBox mascBox;
    private javax.swing.JTextField nombrePaciente;
    private javax.swing.JTextField nombreUsuario;
    private javax.swing.JTextArea rolUsuario;
    private javax.swing.JTextField telefonoPaciente;
    // End of variables declaration//GEN-END:variables
}
