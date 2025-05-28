/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vista;

import control.AdmDatos;
import control.CitaMedicaJpaController;
import control.PacienteJpaController;
import control.PagoJpaController;
import control.exceptions.IllegalOrphanException;
import control.exceptions.NonexistentEntityException;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import modelo.CitaMedica;
import modelo.ModListCitaPago;
import modelo.ModListPacientesPago;
import modelo.ModTabPago;
import modelo.Pago;
import modelo.Paciente;


/**
 *
 * @author carlo
 */
public class PanelPagos extends javax.swing.JPanel {

    //Declaración de variables modificables
    private CitaMedicaJpaController citaMedicaController;
    private PacienteJpaController pacienteController;
    private PagoJpaController pagoController;
    //Variables usadas para desplegar el formulario deslizante
    private Timer timerDeslizamiento;
    private boolean desplegado = false;
    private final int anchoObjetivo = 490; // Ancho deseado del formulario
    private final int velocidadDeslizamiento = 25; // Aumentado para animación más fluida
    private final int anchoActual = 0;
    // variables para manejo de datos
    private final AdmDatos admDatos = new AdmDatos();
    private List<Pago> pagos;
    private ModTabPago modelo;
    private List <Paciente> pacientes;
    private ModListPacientesPago listaPacientes;
    private List <CitaMedica> citas;
    private ModListCitaPago listaFoliosPago;
    //Seleccion de items
    private Paciente pacienteSeleccionado;
    private CitaMedica citaSeleccionada;
    
    public PanelPagos() {
        initComponents();
        inicializarControladores();
        cargarHistorialPagos();
        cargarListaPacientes();
        cargarListaCitas();
        cargarMetodosPago();
        cargarEstadosPago();
        configurarPanelDeslizante();
        configurarComponentesBuscador();
        configurarListeners();
        botonActualizar.setEnabled(false); 
    }

    private void inicializarControladores() {
        //Se usan controladores de pago y paciente
        citaMedicaController = new CitaMedicaJpaController(admDatos.getEmf()); 
        pacienteController = new PacienteJpaController(admDatos.getEmf());
        pagoController = new PagoJpaController(admDatos.getEmf()); 
    }
  
    private void cargarHistorialPagos() {
        pagos = pagoController.findPagoEntities(); 
        pagos.sort(Comparator.comparing(Pago::getIdPago));
        modelo = new ModTabPago(pagos);
        tablaHistorial.setModel(modelo);

    }

    private void cargarListaPacientes() {
        pacientes = pacienteController.findPacienteEntities();
        listaPacientes = new ModListPacientesPago(pacientes);
        jListPacientes.setModel(listaPacientes);
    }
    
    private void cargarListaCitas() {
    citas = citaMedicaController.findCitaMedicaEntities();
    listaFoliosPago = new ModListCitaPago(citas); 
    jListCitas.setModel(listaFoliosPago);
    }
    
    private void cargarMetodosPago() {
        //Está escrito así porque hay un check en la base de datos y si le movemos no deja hacer nada
        metodoPagoComboBox.removeAllItems();
        metodoPagoComboBox.addItem("efectivo");
        metodoPagoComboBox.addItem("tarjeta_credito");
        metodoPagoComboBox.addItem("tarjeta_debito");
        metodoPagoComboBox.addItem("transferencia");
    }

    private void cargarEstadosPago(){
        //Está escrito así porque hay un check en la base de datos y si le movemos no deja hacer nada
        estadoPagoComboBox.removeAllItems(); 
        estadoPagoComboBox.addItem("pendiente");
        estadoPagoComboBox.addItem("completado");
        estadoPagoComboBox.addItem("rechazado");
        estadoPagoComboBox.addItem("reembolsado");
    }
    
    private void configurarPanelDeslizante() {
        Dimension preferredSize = new Dimension(0, panelFormularioDeslizante.getHeight());
        panelFormularioDeslizante.setPreferredSize(preferredSize);
        panelFormularioDeslizante.setMinimumSize(preferredSize);
        panelFormularioDeslizante.setMaximumSize(preferredSize);
        panelFormularioDeslizante.revalidate();
        panelFormularioDeslizante.repaint();
    }

    private void configurarComponentesBuscador() {
        buscador.setJTable(tablaHistorial);
        buscadorListaPacientes.setListaPrincipal(jListPacientes);
        buscadorListaCitas.setListaPrincipal(jListCitas);
    }
    
    private void actualizarTablaPagos() {
        pagos = pagoController.findPagoEntities();
        pagos.sort(Comparator.comparing(Pago::getIdPago));
        modelo.actualizar(pagos); // Asegúrate de que ModTabPago tenga este método
    }
    
    //INICIO DE MÉTODOS PARA EL PANEL DESLIZANTE
    private void desplegarFormulario() {
        desplegado = true;
        timerDeslizamiento.start();
    }
    private void retraerFormulario() {
        desplegado = false;
        timerDeslizamiento.start();
        // Restablecer campos
        jListPacientes.setEnabled(true);
        jListCitas.setEnabled(true);
        txtMonto.setEnabled(true);
    }
    private void animarDeslizamiento() {
        int anchoActual = panelFormularioDeslizante.getPreferredSize().width;
        int nuevoAncho;
        
        if (desplegado) {
            nuevoAncho = Math.min(anchoActual + velocidadDeslizamiento, anchoObjetivo);
        } else {
            nuevoAncho = Math.max(anchoActual - velocidadDeslizamiento, 0);
        }
        
        // Actualizar el tamaño del panel
        panelFormularioDeslizante.setPreferredSize(new Dimension(nuevoAncho, panelFormularioDeslizante.getHeight()));
        panelFormularioDeslizante.setMinimumSize(new Dimension(nuevoAncho, panelFormularioDeslizante.getHeight()));
        panelFormularioDeslizante.setMaximumSize(new Dimension(nuevoAncho, panelFormularioDeslizante.getHeight()));
        
        // Refrescar la visualización
        panelFormularioDeslizante.revalidate();
        panelFormularioDeslizante.repaint();
        revalidate();
        repaint();
        
        // Detener el timer cuando se alcanza el objetivo
        if ((desplegado && nuevoAncho == anchoObjetivo) || (!desplegado && nuevoAncho == 0)) {
            timerDeslizamiento.stop();
        }
    }
    //INICIO DE MÉTODOS FUNCIONALES EN LA INTERFAZ 
    private void configurarListeners() {
        botonAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                desplegarFormulario();
            }
        });

        botonActualizar.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = tablaHistorial.getSelectedRow();
                if (filaSeleccionada != -1) {
                    // Obtener el pago seleccionado desde el modelo
                    Pago pagoSeleccionado = modelo.getPagoAt(filaSeleccionada); 
                    cargarDatosEnFormulario(pagoSeleccionado);
                    desplegarFormulario();
                    tituloPanelDeslizanteLabel.setText("Actualizar Pago");
                }
            }
        });

        timerDeslizamiento = new Timer(15, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animarDeslizamiento();
            }
        });

        botonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                retraerFormulario();
            }
        });
        
            // Listener para la selección de pacientes
        jListPacientes.addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            int selectedIndex = jListPacientes.getSelectedIndex();
            if (selectedIndex != -1) {
                pacienteSeleccionado = pacientes.get(selectedIndex);
                actualizarPacienteSeleccionado(pacienteSeleccionado);
                }
            }
        });

        // Listener para selección de citas
        jListCitas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = jListCitas.getSelectedIndex();
                if (selectedIndex != -1) {
                    citaSeleccionada = listaFoliosPago.getCitaAt(selectedIndex); // Nuevo método en ModListCitaPago
                    actualizarCitaSeleccionada(citaSeleccionada);
                }
            }
        });
    
        botonGuardar.addActionListener(e -> {
            if (validarCampos()) {
                try {
                    int filaSeleccionada = tablaHistorial.getSelectedRow();
                    Pago pagoActualizado = modelo.getPagoAt(filaSeleccionada);

                    // ▶️ Solo actualizar campos permitidos:
                    pagoActualizado.setMonto(new BigDecimal(txtMonto.getText()));
                    pagoActualizado.setMetodoPago((String) metodoPagoComboBox.getSelectedItem());
                    pagoActualizado.setEstatus((String) estadoPagoComboBox.getSelectedItem());

                    // ❌ No modificar referencias a Cita, Paciente o Factura
                    pagoController.edit(pagoActualizado);

                    JOptionPane.showMessageDialog(this, "¡Pago actualizado correctamente!");
                    actualizarTablaPagos();
                    retraerFormulario();
                } catch (IllegalOrphanException ex) {
                    JOptionPane.showMessageDialog(this, "Error: No se puede eliminar la factura asociada.");
                } catch (NonexistentEntityException ex) {
                    JOptionPane.showMessageDialog(this, "Error: El pago ya no existe en la base de datos.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage());
                }
            }
        });
        

        
        tablaHistorial.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int filaSeleccionada = tablaHistorial.getSelectedRow();
                botonActualizar.setEnabled(filaSeleccionada != -1); // Habilitar si hay selección
            }
        });

    }
   
    private void cargarDatosEnFormulario(Pago pago) {
            // Restablecer selecciones
        jListPacientes.clearSelection();
        jListCitas.clearSelection();
        // Bloquear campos no editables
        jListPacientes.setEnabled(false);
        jListCitas.setEnabled(false);
        txtMonto.setEnabled(true); // Permitir editar monto

        // Cargar datos del pago
        pacienteSeleccionado = pago.getIdPaciente();
        citaSeleccionada = pago.getIdCita();

        // Mostrar datos en etiquetas
        pacienteSeleccionadoLabel.setText("Paciente: " + pacienteSeleccionado.getNombre());
        citaSeleccionadaLabel.setText("Cita: #" + citaSeleccionada.getIdCita());
        folioLabel.setText("Folio: " + pago.getIdPago());

        // Cargar valores editables
        txtMonto.setText(pago.getMonto().toString());
        metodoPagoComboBox.setSelectedItem(pago.getMetodoPago());
        estadoPagoComboBox.setSelectedItem(pago.getEstatus());
    }
    
    private void actualizarPacienteSeleccionado(Paciente paciente) {
        if (paciente != null) {
            // Actualizar la etiqueta del paciente
            pacienteSeleccionadoLabel.setText("Paciente Seleccionado: " 
                + paciente.getNombre() + " " + paciente.getApellidoPaterno());

            // Filtrar citas por el paciente seleccionado
            List<CitaMedica> citasFiltradas = citas.stream()
                .filter(c -> c.getIdPaciente().equals(paciente))
                .collect(Collectors.toList());

            // Actualizar la lista de citas
            listaFoliosPago.actualizar(citasFiltradas);
        }
    }
    
    private void actualizarCitaSeleccionada(CitaMedica cita) {
        if (cita != null) {
            citaSeleccionadaLabel.setText("Cita Seleccionada: " + cita.getIdCita());
            folioLabel.setText("Folio de Pago: " + cita.getIdCita()); // Mismo ID para folio
        } else {
            citaSeleccionadaLabel.setText("Cita Seleccionada: ----");
            folioLabel.setText("Folio de Pago: ----");
        }
    }

    private String generarReferencia(String metodoPago) {
        // Obtener las primeras 3 letras del método en mayúsculas
        String prefijo = metodoPago.substring(0, 3).toUpperCase();
        // Número único (usar timestamp)
        String sufijo = String.valueOf(System.currentTimeMillis()).substring(7); // Últimos 5 dígitos
        return prefijo + "-" + sufijo;
    }
    
    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        // Validar solo campos editables en actualización
        if (txtMonto.getText().isEmpty()) {
            errores.append("- Ingrese el monto.\n");
        } else {
            try {
                new BigDecimal(txtMonto.getText());
            } catch (NumberFormatException e) {
                errores.append("- Monto inválido.\n");
            }
        }

        if (metodoPagoComboBox.getSelectedItem() == null) {
            errores.append("- Seleccione un método de pago.\n");
        }

        if (estadoPagoComboBox.getSelectedItem() == null) {
            errores.append("- Seleccione un estado.\n");
        }

        if (errores.length() > 0) {
            JOptionPane.showMessageDialog(this, "Errores:\n" + errores.toString());
            return false;
        }
        return true;
    }
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelFormularioDeslizante = new javax.swing.JPanel();
        tituloPanelDeslizanteLabel = new javax.swing.JLabel();
        nombrePacienteLabel = new javax.swing.JLabel();
        numeroFolioLabel = new javax.swing.JLabel();
        folioLabel = new javax.swing.JLabel();
        estadoLabel = new javax.swing.JLabel();
        montoLabel = new javax.swing.JLabel();
        buscadorListaPacientes = new miscomponentes.JBuscadorLista();
        buscadorListaCitas = new miscomponentes.JBuscadorLista();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListPacientes = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        jListCitas = new javax.swing.JList<>();
        jLabel6 = new javax.swing.JLabel();
        pacienteSeleccionadoLabel = new javax.swing.JLabel();
        citaSeleccionadaLabel = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        botonGuardar = new javax.swing.JButton();
        botonCancelar = new javax.swing.JButton();
        estadoPagoComboBox = new javax.swing.JComboBox<>();
        folioLabel1 = new javax.swing.JLabel();
        metodoPagoComboBox = new javax.swing.JComboBox<>();
        txtMonto = new javax.swing.JTextField();
        panelPrincipal = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaHistorial = new javax.swing.JTable();
        buscador = new miscomponentes.JBuscador();
        botonAgregar = new javax.swing.JButton();
        botonActualizar = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1397, 882));

        panelFormularioDeslizante.setBackground(new java.awt.Color(249, 249, 249));

        tituloPanelDeslizanteLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        tituloPanelDeslizanteLabel.setForeground(new java.awt.Color(0, 0, 0));
        tituloPanelDeslizanteLabel.setText("Agregar Pago");

        nombrePacienteLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        nombrePacienteLabel.setForeground(new java.awt.Color(0, 0, 0));
        nombrePacienteLabel.setText("Nombre del paciente");

        numeroFolioLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        numeroFolioLabel.setForeground(new java.awt.Color(0, 0, 0));
        numeroFolioLabel.setText("Número de cita");

        folioLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        folioLabel.setForeground(new java.awt.Color(0, 0, 0));
        folioLabel.setText("Folio de pago:");

        estadoLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        estadoLabel.setForeground(new java.awt.Color(0, 0, 0));
        estadoLabel.setText("Estado del pago");

        montoLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        montoLabel.setForeground(new java.awt.Color(0, 0, 0));
        montoLabel.setText("Monto Total:");

        jScrollPane2.setViewportView(jListPacientes);

        jListCitas.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jScrollPane3.setViewportView(jListCitas);

        jLabel6.setFont(new java.awt.Font("Dialog", 2, 10)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setText("Si no encuentra al paciente, posiblemente no esté registrado");

        pacienteSeleccionadoLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        pacienteSeleccionadoLabel.setForeground(new java.awt.Color(0, 0, 0));
        pacienteSeleccionadoLabel.setText("Paciente Seleccionado: ....");

        citaSeleccionadaLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        citaSeleccionadaLabel.setForeground(new java.awt.Color(0, 0, 0));
        citaSeleccionadaLabel.setText("Cita Seleccionada: ");

        jLabel7.setFont(new java.awt.Font("Dialog", 2, 10)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("Si no encuentra la cita, posiblemente no esté registrada");

        botonGuardar.setText("Guardar");
        botonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGuardarActionPerformed(evt);
            }
        });

        botonCancelar.setBackground(new java.awt.Color(153, 0, 0));
        botonCancelar.setForeground(new java.awt.Color(255, 255, 255));
        botonCancelar.setText("Cancelar");
        botonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarActionPerformed(evt);
            }
        });

        folioLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        folioLabel1.setForeground(new java.awt.Color(0, 0, 0));
        folioLabel1.setText("Método de pago:");

        javax.swing.GroupLayout panelFormularioDeslizanteLayout = new javax.swing.GroupLayout(panelFormularioDeslizante);
        panelFormularioDeslizante.setLayout(panelFormularioDeslizanteLayout);
        panelFormularioDeslizanteLayout.setHorizontalGroup(
            panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                        .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                                .addComponent(pacienteSeleccionadoLabel)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(nombrePacienteLabel)
                                .addGap(18, 18, 18)
                                .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(buscadorListaPacientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane2)
                                    .addComponent(jScrollPane3)
                                    .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                                        .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(estadoPagoComboBox, 0, 216, Short.MAX_VALUE)
                                                .addComponent(metodoPagoComboBox, 0, 216, Short.MAX_VALUE)
                                                .addComponent(txtMonto))
                                            .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(botonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addContainerGap())
                    .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                                .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(citaSeleccionadaLabel)
                                    .addComponent(numeroFolioLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buscadorListaCitas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                                .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(folioLabel)
                                    .addComponent(folioLabel1)
                                    .addComponent(estadoLabel)
                                    .addComponent(montoLabel))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormularioDeslizanteLayout.createSequentialGroup()
                        .addGap(0, 184, Short.MAX_VALUE)
                        .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormularioDeslizanteLayout.createSequentialGroup()
                                .addComponent(tituloPanelDeslizanteLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(157, 157, 157))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormularioDeslizanteLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormularioDeslizanteLayout.createSequentialGroup()
                                .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7))
                                .addGap(32, 32, 32))))))
        );
        panelFormularioDeslizanteLayout.setVerticalGroup(
            panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tituloPanelDeslizanteLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buscadorListaPacientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(nombrePacienteLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addGap(3, 3, 3)
                .addComponent(pacienteSeleccionadoLabel)
                .addGap(18, 18, 18)
                .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buscadorListaCitas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(numeroFolioLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(citaSeleccionadaLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(folioLabel1)
                    .addComponent(metodoPagoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(folioLabel)
                .addGap(24, 24, 24)
                .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(estadoPagoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(estadoLabel))
                .addGap(18, 18, 18)
                .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(montoLabel)
                    .addComponent(txtMonto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(286, 286, 286))
        );

        panelPrincipal.setBackground(new java.awt.Color(255, 255, 255));

        tablaHistorial.setBackground(new java.awt.Color(255, 255, 255));
        tablaHistorial.setForeground(new java.awt.Color(153, 153, 153));
        tablaHistorial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "N. de cita", "Folio", "Paciente", "Estado", "Fecha y hora", "Monto"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaHistorial.setGridColor(new java.awt.Color(153, 153, 153));
        jScrollPane1.setViewportView(tablaHistorial);

        buscador.setBackground(new java.awt.Color(204, 204, 204));
        buscador.setForeground(new java.awt.Color(255, 255, 255));
        buscador.setJTable(tablaHistorial);
        buscador.setPlaceholder("Buscar pago...");

        botonAgregar.setText("Agregar Pago");

        botonActualizar.setText("Actualizar Pago");

        javax.swing.GroupLayout panelPrincipalLayout = new javax.swing.GroupLayout(panelPrincipal);
        panelPrincipal.setLayout(panelPrincipalLayout);
        panelPrincipalLayout.setHorizontalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(panelPrincipalLayout.createSequentialGroup()
                        .addComponent(buscador, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 299, Short.MAX_VALUE)
                        .addComponent(botonActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botonAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelPrincipalLayout.setVerticalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(botonActualizar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buscador, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botonAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 709, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelFormularioDeslizante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(panelPrincipal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelFormularioDeslizante, javax.swing.GroupLayout.PREFERRED_SIZE, 826, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void botonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGuardarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonGuardarActionPerformed

    private void botonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonCancelarActionPerformed

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonActualizar;
    private javax.swing.JButton botonAgregar;
    private javax.swing.JButton botonCancelar;
    private javax.swing.JButton botonGuardar;
    private miscomponentes.JBuscador buscador;
    private miscomponentes.JBuscadorLista buscadorListaCitas;
    private miscomponentes.JBuscadorLista buscadorListaPacientes;
    private javax.swing.JLabel citaSeleccionadaLabel;
    private javax.swing.JLabel estadoLabel;
    private javax.swing.JComboBox<String> estadoPagoComboBox;
    private javax.swing.JLabel folioLabel;
    private javax.swing.JLabel folioLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList<String> jListCitas;
    private javax.swing.JList<String> jListPacientes;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JComboBox<String> metodoPagoComboBox;
    private javax.swing.JLabel montoLabel;
    private javax.swing.JLabel nombrePacienteLabel;
    private javax.swing.JLabel numeroFolioLabel;
    private javax.swing.JLabel pacienteSeleccionadoLabel;
    private javax.swing.JPanel panelFormularioDeslizante;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JTable tablaHistorial;
    private javax.swing.JLabel tituloPanelDeslizanteLabel;
    private javax.swing.JTextField txtMonto;
    // End of variables declaration//GEN-END:variables


}
