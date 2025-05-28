/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vista;

import control.AdmDatos;
import control.InventarioJpaController;
import control.MedicamentoJpaController;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import modelo.Inventario;
import modelo.Medicamento;
import modelo.ModListMedicamentoInventario;
import modelo.ModTabInventario;
import com.toedter.calendar.IDateEvaluator;
/**
 *
 * @author carlo
 */
public class PanelInventario extends javax.swing.JPanel {
    //Variables de la tabla 
    private final AdmDatos admDatos = new AdmDatos();
    private final InventarioJpaController inventarioController;
    private List<Inventario> inventario;
    private ModTabInventario modelo;
    //Variables para inicializar la lista de medicamentos
    private final MedicamentoJpaController medicamentoController;
    private ModListMedicamentoInventario modListaMedicamentos;
    private List<Medicamento> medicamentos;
    //Variables usadas para inicializar y modificar el panel deslizante
    private Timer timerDeslizamiento;
    private boolean desplegado = false;
    private final int anchoObjetivo = 490; // Ancho deseado del formulario
// Ancho deseado del formulario
    private final int velocidadDeslizamiento = 25;
    // Nuevo campo para rastrear el ancho actual
    // Aumentado para animación más fluida
    private Medicamento medicamentoSeleccionado = null;
    private Inventario inventarioSeleccionado = null;
    /**
     * Creates new form PanelInventario
     */
    public PanelInventario() {
    initComponents();
        initComponents();
        inventarioController = new InventarioJpaController(admDatos.getEmf());
        medicamentoController = new MedicamentoJpaController(admDatos.getEmf());
        timerDeslizamiento = new Timer(10, e -> animarDeslizamiento());
        configurarInicialmente();
        cargarTablaInventario();
        cargarListaMedicamentos();
        configurarPanelDeslizante();
        configurarListeners();
        configurarCalendar(); // <-- Añadir esta línea
        botonEditar.setEnabled(false);
        botonEliminar.setEnabled(false);
        //Colores de la tabla
        tablaInventario.setShowGrid(true);
        tablaInventario.setGridColor(new Color(0xd0d0d0));
        tablaInventario.setRowHeight(25); // Aumentar altura de filas para mejor visibilidad
        tablaInventario.setIntercellSpacing(new Dimension(1, 1)); // Espaciado entre celdas
    }

    private void configurarInicialmente() {
        // Configurar placeholders
        cantidadField.setText("");
        ubicacionField.setText("");
        loteField.setText("");

        // Configurar hints (placeholders)
        setPlaceholder(cantidadField, "Cantidad");
        setPlaceholder(ubicacionField, "Ubicación");
        setPlaceholder(loteField, "Lote");

        // Ocultar panel deslizante inicialmente
        panelFormularioDeslizante.setVisible(false);
}
    
    private void setPlaceholder(JTextField field, String placeholder) {
        field.setForeground(Color.GRAY);
        field.setText(placeholder);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }
    
    private void cargarTablaInventario() {
        inventario = inventarioController.findInventarioEntities();
        // Ordenar: primero caducidad próxima, luego cantidad <5
        inventario.sort((i1, i2) -> {
            boolean i1Caducidad = esCaducidadProxima(i1);
            boolean i2Caducidad = esCaducidadProxima(i2);
            boolean i1Cantidad = i1.getCantidad() < 5;
            boolean i2Cantidad = i2.getCantidad() < 5;

            // Prioridad 1: Caducidad próxima
            if (i1Caducidad != i2Caducidad) {
                return i2Caducidad ? 1 : -1;
            }
            // Prioridad 2: Cantidad baja
            if (i1Cantidad != i2Cantidad) {
                return i2Cantidad ? 1 : -1;
            }
            return i1.getIdInventario().compareTo(i2.getIdInventario());
        });
           modelo = new ModTabInventario(inventario);
           tablaInventario.setModel(modelo);

            // Aplicar renderizado personalizado
            renderTabla();

            // Forzar actualización visual
            tablaInventario.repaint();
            tablaInventario.revalidate();

        }
    
    private void cargarListaMedicamentos() {
        medicamentos = medicamentoController.findMedicamentoEntities();
        modListaMedicamentos = new ModListMedicamentoInventario(medicamentos);
        listaMedicamento.setModel(modListaMedicamentos);
        buscadorListaMedicamento.setListaPrincipal(listaMedicamento);
    }
    
    private void actualizarTabla() {
        modelo.fireTableDataChanged(); // Notificar cambios al modelo
        tablaInventario.repaint();
        tablaInventario.revalidate();
    }
   
    private void configurarListeners() {        
        // Listener para la selección en la tabla
        tablaInventario.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tablaInventario.getSelectedRow();
                if (selectedRow >= 0) {
                    inventarioSeleccionado = inventario.get(selectedRow);
                    botonEditar.setEnabled(true); // Habilitar Editar
                    botonEliminar.setEnabled(true); // Habilitar Eliminar
                } else {
                    botonEditar.setEnabled(false);
                    botonEliminar.setEnabled(false);
                }
            }
        });

        // Listener para el botón Editar
        botonEditar.addActionListener(e -> {
            if (inventarioSeleccionado != null) {
                cargarDatosEnFormulario(inventarioSeleccionado);
                desplegarFormulario();
                tituloPanelLabel.setText("Editar Medicamento en Inventario");
                listaMedicamento.setEnabled(false);
            }
        });

        // Listener para el botón Agregar
        botonAgregar.addActionListener(e -> {
            limpiarCampos();
            desplegarFormulario();
            tituloPanelLabel.setText("Agregar Nuevo Medicamento al Inventario");
        });
    
        // Listener para la selección en la lista de medicamentos
        listaMedicamento.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = listaMedicamento.getSelectedIndex();
                if (selectedIndex >= 0) {
                    medicamentoSeleccionado = modListaMedicamentos.getMedicamentoAt(selectedIndex);
                    medicamentoSeleccionadoLabel.setText(medicamentoSeleccionado.getNombre());
                }
            }
        });
    
            // Listener para el calendario
            jCalendar1.addPropertyChangeListener("calendar", evt -> {
                java.util.Calendar cal = jCalendar1.getCalendar();
                if (cal != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    jLabel1.setText("Fecha Seleccionada: " + sdf.format(cal.getTime()));
                }
            });

        // Listener para los botones Cancelar y Guardar
        botonCancelar.addActionListener(e -> retraerFormulario());
        botonGuardar.addActionListener(e -> guardarDatos());
    }

    private void configurarCalendar() {
        // Obtener la fecha actual
        java.util.Calendar cal = java.util.Calendar.getInstance();

        // Configurar fecha mínima seleccionable (hoy)
        jCalendar1.setMinSelectableDate(cal.getTime());

        // Listener para validar la selección de fecha
        jCalendar1.addPropertyChangeListener("calendar", evt -> {
            java.util.Calendar selectedCal = jCalendar1.getCalendar();
            if (selectedCal != null) {
                // Si la fecha seleccionada es anterior a hoy
                if (selectedCal.before(cal)) {
                    // Revertir a la fecha actual
                    jCalendar1.setCalendar(cal);
                    JOptionPane.showMessageDialog(this, 
                        "No se puede seleccionar fechas anteriores a hoy", 
                        "Error de fecha", 
                        JOptionPane.WARNING_MESSAGE);
                }

                // Actualizar la etiqueta de fecha seleccionada
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                jLabel1.setText("Fecha Seleccionada: " + sdf.format(selectedCal.getTime()));
            }
        });
    }
    private void cargarDatosEnFormulario(Inventario inv) {
        if (inv != null) {
            // Cargar medicamento
            medicamentoSeleccionado = inv.getIdMedicamento();
            if (medicamentoSeleccionado != null) {
                medicamentoSeleccionadoLabel.setText(medicamentoSeleccionado.getNombre());

                // Seleccionar el medicamento en la lista
                for (int i = 0; i < modListaMedicamentos.getSize(); i++) {
                    if (modListaMedicamentos.getMedicamentoAt(i).equals(medicamentoSeleccionado)) {
                        listaMedicamento.setSelectedIndex(i);
                        break;
                    }
                }
            }

            // Cargar otros datos
            cantidadField.setText(String.valueOf(inv.getCantidad()));
            ubicacionField.setText(inv.getUbicacion());
            loteField.setText(inv.getLote());

            // Cargar fecha de caducidad
            if (inv.getFechaCaducidad() != null) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(inv.getFechaCaducidad());

                // Si la fecha es anterior a hoy, mostrar hoy como fecha mínima
                java.util.Calendar hoy = java.util.Calendar.getInstance();
                if (cal.before(hoy)) {
                    cal = hoy;
                }

                jCalendar1.setCalendar(cal);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                jLabel1.setText("Fecha Seleccionada: " + sdf.format(cal.getTime()));
            }
        }
    }
    
    private void guardarDatos() {
        // Implementa la lógica para guardar los datos
        // Puedes diferenciar entre edición y nuevo registro usando inventarioSeleccionado

        try {
            if (medicamentoSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un medicamento", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar campos
            int cantidad;
            try {
                cantidad = Integer.parseInt(cantidadField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String ubicacion = ubicacionField.getText();
            String lote = loteField.getText();

            if (ubicacion.isEmpty() || ubicacion.equals("Ubicación")) {
                JOptionPane.showMessageDialog(this, "Ingrese una ubicación válida", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (lote.isEmpty() || lote.equals("Lote")) {
                JOptionPane.showMessageDialog(this, "Ingrese un lote válido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener fecha del calendario
            java.util.Calendar cal = jCalendar1.getCalendar();
            Date fechaCaducidad = cal != null ? cal.getTime() : null;

            if (tituloPanelLabel.getText().startsWith("Editar")) {
                // Modo edición
                inventarioSeleccionado.setCantidad(cantidad);
                inventarioSeleccionado.setUbicacion(ubicacion);
                inventarioSeleccionado.setLote(lote);
                inventarioSeleccionado.setFechaCaducidad(fechaCaducidad);

                inventarioController.edit(inventarioSeleccionado);
                JOptionPane.showMessageDialog(this, "Inventario actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Modo nuevo
                Inventario nuevoInventario = new Inventario();
                nuevoInventario.setIdMedicamento(medicamentoSeleccionado);
                nuevoInventario.setCantidad(cantidad);
                nuevoInventario.setUbicacion(ubicacion);
                nuevoInventario.setLote(lote);
                nuevoInventario.setFechaCaducidad(fechaCaducidad);

                inventarioController.create(nuevoInventario);
                JOptionPane.showMessageDialog(this, "Medicamento agregado al inventario correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }

            // Actualizar tabla y retraer formulario
            cargarTablaInventario();
            retraerFormulario();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar los datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private boolean esCaducidadProxima(Inventario inv) {
        if (inv.getFechaCaducidad() == null) return false;
        LocalDate fechaCad = inv.getFechaCaducidad().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate hoy = LocalDate.now();
        long diasRestantes = ChronoUnit.DAYS.between(hoy, fechaCad);
        return diasRestantes <= 30; // 1 mes = 30 días
    }

    private boolean estaVencido(Inventario inv) {
        if (inv.getFechaCaducidad() == null) return false;
        LocalDate fechaCad = inv.getFechaCaducidad().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate hoy = LocalDate.now();
        return hoy.isAfter(fechaCad);
    }

    private void renderTabla() {
        tablaInventario.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Inventario inv = inventario.get(row);

                // Configurar colores que funcionen bien con FlatLaf
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                } else {
                    boolean vencido = estaVencido(inv);
                    boolean cadProxima = esCaducidadProxima(inv);
                    boolean cantBaja = inv.getCantidad() < 5;

                    if (vencido) {
                        c.setBackground(new Color(255, 200, 200));  // Rojo claro
                        c.setForeground(Color.BLACK);
                    } else if (cadProxima || cantBaja) {
                        c.setBackground(new Color(255, 255, 150));  // Amarillo claro
                        c.setForeground(Color.BLACK);
                    } else {
                        c.setBackground(table.getBackground());
                        c.setForeground(table.getForeground());
                    }
                }
                return c;
            }
        });
    }
    
    private void configurarPanelDeslizante() {
        Dimension preferredSize = new Dimension(0, panelFormularioDeslizante.getHeight());
        panelFormularioDeslizante.setPreferredSize(preferredSize);
        panelFormularioDeslizante.setMinimumSize(preferredSize);
        panelFormularioDeslizante.setMaximumSize(preferredSize);
        panelFormularioDeslizante.revalidate();
        panelFormularioDeslizante.repaint();
    }
    private void desplegarFormulario() {
        desplegado = true;
        panelFormularioDeslizante.setVisible(true);
        timerDeslizamiento.start();
    }
    private void retraerFormulario() {
        desplegado = false;
        timerDeslizamiento.start();
    }
    private void limpiarCampos() {
        medicamentoSeleccionado = null;
        inventarioSeleccionado = null;
        medicamentoSeleccionadoLabel.setText("..");
        cantidadField.setText("");
        ubicacionField.setText("");
        loteField.setText("");
        jLabel1.setText("Fecha Seleccionada:");
        listaMedicamento.clearSelection();

        // Establecer la fecha actual como mínima
        java.util.Calendar cal = java.util.Calendar.getInstance();
        jCalendar1.setCalendar(cal);

        // Restaurar placeholders
        setPlaceholder(cantidadField, "Cantidad");
        setPlaceholder(ubicacionField, "Ubicación");
        setPlaceholder(loteField, "Lote");

        // Habilitar lista de medicamentos si estaba deshabilitada
        listaMedicamento.setEnabled(true);
    }
    private void animarDeslizamiento() {
    int anchoActual = panelFormularioDeslizante.getWidth();
        int nuevoAncho;

        if (desplegado) {
            nuevoAncho = Math.min(anchoActual + velocidadDeslizamiento, anchoObjetivo);
        } else {
            nuevoAncho = Math.max(anchoActual - velocidadDeslizamiento, 0);
        }

        panelFormularioDeslizante.setPreferredSize(new Dimension(nuevoAncho, panelFormularioDeslizante.getHeight()));
        panelFormularioDeslizante.setSize(new Dimension(nuevoAncho, panelFormularioDeslizante.getHeight()));

        revalidate();
        repaint();

        if ((desplegado && nuevoAncho == anchoObjetivo) || (!desplegado && nuevoAncho == 0)) {
            timerDeslizamiento.stop();
            if (!desplegado) {
                panelFormularioDeslizante.setVisible(false);
            }
        }
    }
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelFormularioDeslizante = new javax.swing.JPanel();
        tituloPanelLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listaMedicamento = new javax.swing.JList<>();
        buscadorListaMedicamento = new miscomponentes.JBuscadorLista();
        tituloPanelLabel1 = new javax.swing.JLabel();
        jCalendar1 = new com.toedter.calendar.JCalendar();
        cantidadField = new javax.swing.JTextField();
        fechaLabel = new javax.swing.JLabel();
        medSeleccionLabel = new javax.swing.JLabel();
        medicamentoSeleccionadoLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        ubicacionField = new javax.swing.JTextField();
        loteField = new javax.swing.JTextField();
        botonCancelar = new javax.swing.JButton();
        botonGuardar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        botonEliminar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaInventario = new javax.swing.JTable();
        jBuscador1 = new miscomponentes.JBuscador();
        botonEditar = new javax.swing.JButton();
        botonAgregar = new javax.swing.JButton();

        tituloPanelLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        tituloPanelLabel.setText("Título Panel");

        jScrollPane2.setViewportView(listaMedicamento);

        tituloPanelLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tituloPanelLabel1.setText("Buscar Medicamento");

        cantidadField.setText("Cantidad");
        cantidadField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cantidadFieldActionPerformed(evt);
            }
        });

        fechaLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        fechaLabel.setText("Seleccione la fecha de Caducidad");

        medSeleccionLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        medSeleccionLabel.setText("Medicamento seleccionado: ");

        medicamentoSeleccionadoLabel.setText("..");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Fecha Seleccionada:");

        ubicacionField.setText("Ubicación ");
        ubicacionField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ubicacionFieldActionPerformed(evt);
            }
        });

        loteField.setText("Lote");

        botonCancelar.setBackground(new java.awt.Color(153, 0, 0));
        botonCancelar.setForeground(new java.awt.Color(255, 255, 255));
        botonCancelar.setText("Cancelar");
        botonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarActionPerformed(evt);
            }
        });

        botonGuardar.setText("Guardar");

        javax.swing.GroupLayout panelFormularioDeslizanteLayout = new javax.swing.GroupLayout(panelFormularioDeslizante);
        panelFormularioDeslizante.setLayout(panelFormularioDeslizanteLayout);
        panelFormularioDeslizanteLayout.setHorizontalGroup(
            panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormularioDeslizanteLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tituloPanelLabel)
                .addGap(107, 107, 107))
            .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                        .addComponent(botonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(121, 121, 121))
                    .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                        .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tituloPanelLabel1)
                            .addComponent(medSeleccionLabel)
                            .addComponent(jLabel1)
                            .addComponent(fechaLabel)
                            .addComponent(medicamentoSeleccionadoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buscadorListaMedicamento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                            .addComponent(jScrollPane2)
                            .addComponent(jCalendar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ubicacionField)
                            .addComponent(cantidadField)
                            .addComponent(loteField))
                        .addContainerGap())))
        );
        panelFormularioDeslizanteLayout.setVerticalGroup(
            panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormularioDeslizanteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tituloPanelLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tituloPanelLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscadorListaMedicamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(medSeleccionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(medicamentoSeleccionadoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fechaLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCalendar1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cantidadField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ubicacionField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addGroup(panelFormularioDeslizanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonGuardar)
                    .addComponent(botonCancelar))
                .addGap(36, 36, 36))
        );

        botonEliminar.setText("Eliminar");

        tablaInventario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Medicamento", "Nombre", "Cantidad", "Lote", "Caducidad", "Precio al público", "Ubicación"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaInventario);

        botonEditar.setText("Editar");

        botonAgregar.setText("Agregar");
        botonAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAgregarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jBuscador1, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                        .addComponent(botonAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botonEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(botonEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(botonEliminar, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(botonAgregar)
                            .addComponent(botonEditar)))
                    .addComponent(jBuscador1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelFormularioDeslizante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelFormularioDeslizante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cantidadFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cantidadFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cantidadFieldActionPerformed

    private void ubicacionFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ubicacionFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ubicacionFieldActionPerformed

    private void botonAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAgregarActionPerformed

    }//GEN-LAST:event_botonAgregarActionPerformed

    private void botonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarActionPerformed

    }//GEN-LAST:event_botonCancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonAgregar;
    private javax.swing.JButton botonCancelar;
    private javax.swing.JButton botonEditar;
    private javax.swing.JButton botonEliminar;
    private javax.swing.JButton botonGuardar;
    private miscomponentes.JBuscadorLista buscadorListaMedicamento;
    private javax.swing.JTextField cantidadField;
    private javax.swing.JLabel fechaLabel;
    private miscomponentes.JBuscador jBuscador1;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> listaMedicamento;
    private javax.swing.JTextField loteField;
    private javax.swing.JLabel medSeleccionLabel;
    private javax.swing.JLabel medicamentoSeleccionadoLabel;
    private javax.swing.JPanel panelFormularioDeslizante;
    private javax.swing.JTable tablaInventario;
    private javax.swing.JLabel tituloPanelLabel;
    private javax.swing.JLabel tituloPanelLabel1;
    private javax.swing.JTextField ubicacionField;
    // End of variables declaration//GEN-END:variables
}
