/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package calculodebarras;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author ItielSanz<ItielSanzAXO>
 */
public class Panel extends javax.swing.JFrame {

    /**
     * Creates new form Panel
     */
    private int tabIndex = -1; // Inicializa con un valor que no se corresponde con ningún índice de pestaña
  
    public Panel() {
        initComponents();
        llenarComboBox();
        validarCampos();
        configurarListeners();
        actualizarTabla();
        
         // Llama al método para configurar el ListSelectionListener
        selectTabla();

        // Asegurarse de que no haya filas seleccionadas al cargar el panel
        tblRegistros.clearSelection();
        
    }
    
    // Método para configurar el ListSelectionListener
    private void selectTabla() {
        tblRegistros.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Verificar si hay al menos una fila seleccionada
                if (tblRegistros.getSelectedRow() != -1) {
                    btnEliminar.setEnabled(true); // Habilitar el botón si hay una fila seleccionada
                } else {
                    btnEliminar.setEnabled(false); // Deshabilitar el botón si no hay filas seleccionadas
                }
            }
        });
        
        // Agrega un enfoque (focus) a la tabla tblRegistros
    //tblRegistros.addFocusListener(new FocusAdapter() {
    //   @Override
    //  public void focusLost(FocusEvent e) {
            // Deshabilita el botón "btnEliminar" cuando la tabla pierde el enfoque
     //     btnEliminar.setEnabled(false);
            //COMETANDO BOTON ELIMINARtblRegistros.clearSelection();
     // }
    //});
    
    
        // Agrega un ChangeListener al JTabbedPane para deseleccionar las filas al cambiar de pestaña
    jTabbedPane2.addChangeListener(new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            // Verifica si el índice de la pestaña actual es diferente de la anterior
            if (jTabbedPane2.getSelectedIndex() != tabIndex) {
                // Deseleccionar cualquier fila seleccionada en la tabla
                tblRegistros.clearSelection();
                
                // Actualiza el índice de la pestaña actual
                tabIndex = jTabbedPane2.getSelectedIndex();
                //COMENTADO BOTON 2tblRegistros.clearSelection();
            }
        }
    });
    }
    
// Método para obtener registros de la base de datos y cargarlos en la tabla
private void actualizarTabla() {
    // Define los nombres de las columnas
    String[] columnas = {"No. Orden", "Nombre Técnico", "Se resolvió", "Hora de Inicio", "Hora de Final", "Escalonado a"};

    // Define el modelo de la tabla con nombres de columnas
    DefaultTableModel model = new DefaultTableModel(columnas, 0) {
        // Sobreescribe el método isCellEditable para hacer que todas las celdas sean no editables
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    // Conecta a la base de datos y obtén los registros
    try {
        String connectionString = "jdbc:sqlserver://ingsoftdatabase.database.windows.net:1433;database=IngSoftwareDB;user=admin26@ingsoftdatabase;password=@Axopunk2023.;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        Connection connection = DriverManager.getConnection(connectionString);

        String sql = "SELECT No_Orden, Nom_Tecnico, Se_resolvio, Hr_Inicio, Hr_Final, Escalonado_A FROM Registros";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        // Formatea las horas para que muestren solo horas y minutos
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        
        // Itera a través de los resultados y agrega cada registro a la tabla
        while (resultSet.next()) {
            int noOrden = resultSet.getInt("No_Orden");
            String nombreTecnico = resultSet.getString("Nom_Tecnico");
            String seResolvio = resultSet.getString("Se_resolvio");
            String horaInicio = sdf.format(resultSet.getTime("Hr_Inicio"));
            String horaFinal = sdf.format(resultSet.getTime("Hr_Final"));
            String escalonadoA = resultSet.getString("Escalonado_A");

            // Agrega una fila a la tabla con los datos del registro
            model.addRow(new Object[]{noOrden, nombreTecnico, seResolvio, horaInicio, horaFinal, escalonadoA});
        }

        // Cierra la conexión a la base de datos
        connection.close();
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al obtener los registros de la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    // Deshabilita el movimiento de columnas en el encabezado de la tabla
    tblRegistros.getTableHeader().setReorderingAllowed(false);

    // Asigna el modelo de tabla personalizado a la tabla
    tblRegistros.setModel(model);
}

    private void configurarListeners() {
        cmbResolvio.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String resolvioSeleccionado = (String) cmbResolvio.getSelectedItem();

                    // Verifica si la opción seleccionada es "Si"
                    if ("Si".equals(resolvioSeleccionado)) {
                        cmbEscalonado.setEnabled(false); // Deshabilita el JComboBox de Escalonado
                        cmbEscalonado.setSelectedIndex(0); // Establece la selección en la opción predeterminada
                    } else {
                        cmbEscalonado.setEnabled(true); // Habilita el JComboBox de Escalonado
                    }
                }
            }
        });
    }
    
    private void guardarRegistro(String tecnico, String resolvio, String horaInicio, String horaFinal, String escalonado) {
    // Cadena de conexión a la base de datos
    String connectionString = "jdbc:sqlserver://ingsoftdatabase.database.windows.net:1433;database=IngSoftwareDB;user=admin26@ingsoftdatabase;password=@Axopunk2023.;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    
    // Consulta SQL para insertar un nuevo registro en la tabla "Registros"
    String sql = "INSERT INTO Registros (Nom_Tecnico, Se_resolvio, Hr_Inicio, Hr_Final, Escalonado_A) VALUES (?, ?, ?, ?, ?)";
    
    try {
        // Establecer la conexión a la base de datos
        Connection connection = DriverManager.getConnection(connectionString);
        
        // Crear una sentencia preparada con la consulta SQL
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        
        // Establecer los valores de los parámetros en la sentencia preparada
        preparedStatement.setString(1, tecnico);
        preparedStatement.setString(2, resolvio);
        preparedStatement.setString(3, horaInicio);
        preparedStatement.setString(4, horaFinal);
        
        // Verificar si el campo de Escalonado A tiene un valor válido
        if (!"-Selecciona-".equals(escalonado)) {
            preparedStatement.setString(5, escalonado);
        } else {
            preparedStatement.setNull(5, java.sql.Types.VARCHAR); // Establecer como NULL si no hay valor
        }
        
        // Ejecutar la inserción en la base de datos
        preparedStatement.executeUpdate();
        
        // Cerrar la conexión y la sentencia preparada
        preparedStatement.close();
        connection.close();
        
        // Mostrar un mensaje de éxito
        JOptionPane.showMessageDialog(this, "Registro guardado exitosamente. (POR FIN...)", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        
        // Limpia los campos
        limpiarCampos();
    } catch (SQLException e) {
        e.printStackTrace();
        // Manejo de errores: muestra un mensaje de error en caso de fallo en la inserción
        JOptionPane.showMessageDialog(this, "Error al guardar el registro.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void limpiarCampos() {
    // Restablecer los JComboBox a su valor predeterminado
    cmbTecnico.setSelectedItem("-Selecciona-");
    cmbResolvio.setSelectedItem("-Selecciona-");
    cmbHoraInicio.setSelectedItem("-Selecciona-");
    cmbHoraFinal.setSelectedItem("-Selecciona-");
    cmbEscalonado.setSelectedItem("-Selecciona-");
    
    // Actulizamos la tabla
    actualizarTabla();
}
    
private void validarCampos() {
    // Obtiene los valores seleccionados en los JComboBox
    String resolvio = (String) cmbResolvio.getSelectedItem();
    String escalonado = (String) cmbEscalonado.getSelectedItem();

    // Obtiene los valores seleccionados en los JComboBox de hora
    String horaInicio = (String) cmbHoraInicio.getSelectedItem();
    String horaFinal = (String) cmbHoraFinal.getSelectedItem();

    // Obtiene el valor seleccionado en cmbTecnico
    String tecnico = (String) cmbTecnico.getSelectedItem();

    // Valida las horas
    if (validarHoras(horaInicio, horaFinal)) {
        // Las horas son válidas, continúa con las otras validaciones

        // Verifica si todos los campos están completos y cumple con las condiciones
        if (!"-Selecciona-".equals(tecnico) && !"-Selecciona-".equals(resolvio) &&
            ((resolvio.equals("No") && !"-Selecciona-".equals(escalonado)) || resolvio.equals("Si"))) {
            btnGuardar.setEnabled(true);
        } else {
            btnGuardar.setEnabled(false);
        }
    } else {
        // Las horas no son válidas, desactiva el botón de guardar
        btnGuardar.setEnabled(false);
    }
}

private boolean validarHoras(String horaInicio, String horaFinal) {
    try {
        // Divide las horas en horas y minutos
        String[] partesHoraInicio = horaInicio.split(":");
        String[] partesHoraFinal = horaFinal.split(":");
        
        // Obtén las horas y minutos como enteros
        int horaInicioInt = Integer.parseInt(partesHoraInicio[0]);
        int minutoInicioInt = Integer.parseInt(partesHoraInicio[1]);
        int horaFinalInt = Integer.parseInt(partesHoraFinal[0]);
        int minutoFinalInt = Integer.parseInt(partesHoraFinal[1]);
        
        // Valida que las horas estén en rangos válidos
        if (horaInicioInt >= 0 && horaInicioInt <= 23 && horaFinalInt >= 0 && horaFinalInt <= 23) {
            // Si las horas son iguales, los minutos de inicio deben ser menores que los de fin
            if (horaInicioInt == horaFinalInt) {
                return minutoInicioInt < minutoFinalInt;
            } else if (horaInicioInt < horaFinalInt) {
                return true; // Las horas son válidas
            } else {
                return false; // Hora de inicio mayor que hora final
            }
        } else {
            return false; // Las horas no son válidas
        }
    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
        // Error al convertir o dividir las horas, consideramos que no son válidas
        return false;
    }
}

    private void llenarComboBox() {
        // Configura la conexión a la base de datos
        String connectionString = "jdbc:sqlserver://ingsoftdatabase.database.windows.net:1433;database=IngSoftwareDB;user=admin26@ingsoftdatabase;password=@Axopunk2023.;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        Connection connection = null;

        try {
            // Establece la conexión a la base de datos
            connection = DriverManager.getConnection(connectionString);

            // Consulta SQL para recuperar los nombres de técnicos de la tabla "Tecnicos"
            String sql = "SELECT NombreTecnico FROM Tecnicos";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // Lista para almacenar los nombres de técnicos
            ArrayList<String> nombresTecnicos = new ArrayList<>();

            // Recorre los resultados y agrega los nombres a la lista
            while (resultSet.next()) {
                String nombreTecnico = resultSet.getString("NombreTecnico");
                nombresTecnicos.add(nombreTecnico);
            }

            // Llena los JComboBox con los nombres de técnicos
            for (String nombreTecnico : nombresTecnicos) {
                cmbTecnico.addItem(nombreTecnico);
                //cmbResolvio.addItem(nombreTecnico);
                cmbEscalonado.addItem(nombreTecnico);
            }

            // Agrega la opción "-Selecciona-" como selección inicial
            cmbTecnico.setSelectedItem("-Selecciona-");
            cmbResolvio.setSelectedItem("-Selecciona-");
            cmbEscalonado.setSelectedItem("-Selecciona-");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Cierra la conexión a la base de datos
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane2 = new javax.swing.JTabbedPane();
        pRegistro = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbTecnico = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        cmbResolvio = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cmbEscalonado = new javax.swing.JComboBox<>();
        btnGuardar = new javax.swing.JButton();
        cmbHoraInicio = new javax.swing.JComboBox<>();
        cmbHoraFinal = new javax.swing.JComboBox<>();
        pRegistros = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRegistros = new javax.swing.JTable();
        btnImprimir = new javax.swing.JToggleButton();
        btnEliminar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane2.setBackground(new java.awt.Color(255, 255, 255));

        pRegistro.setBackground(new java.awt.Color(128, 230, 241));

        jLabel1.setText("Nombre del Técnico");

        cmbTecnico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Selecciona-" }));
        cmbTecnico.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTecnicoItemStateChanged(evt);
            }
        });
        cmbTecnico.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                cmbTecnicoPropertyChange(evt);
            }
        });

        jLabel2.setText("¿Se resolvió?");

        cmbResolvio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Selecciona-", "Si", "No" }));
        cmbResolvio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbResolvioItemStateChanged(evt);
            }
        });
        cmbResolvio.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                cmbResolvioPropertyChange(evt);
            }
        });

        jLabel3.setText("Hora de Inicio");

        jLabel4.setText("Hora de Final");

        jLabel5.setText("Escalonado a:");

        cmbEscalonado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Selecciona-" }));
        cmbEscalonado.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbEscalonadoItemStateChanged(evt);
            }
        });
        cmbEscalonado.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                cmbEscalonadoPropertyChange(evt);
            }
        });

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        btnGuardar.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                btnGuardarPropertyChange(evt);
            }
        });

        cmbHoraInicio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Selecciona-", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00" }));
        cmbHoraInicio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbHoraInicioItemStateChanged(evt);
            }
        });
        cmbHoraInicio.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                cmbHoraInicioPropertyChange(evt);
            }
        });

        cmbHoraFinal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Selecciona-", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00" }));
        cmbHoraFinal.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbHoraFinalItemStateChanged(evt);
            }
        });
        cmbHoraFinal.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                cmbHoraFinalPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout pRegistroLayout = new javax.swing.GroupLayout(pRegistro);
        pRegistro.setLayout(pRegistroLayout);
        pRegistroLayout.setHorizontalGroup(
            pRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pRegistroLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(pRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pRegistroLayout.createSequentialGroup()
                        .addGap(264, 264, 264)
                        .addComponent(btnGuardar))
                    .addGroup(pRegistroLayout.createSequentialGroup()
                        .addGroup(pRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pRegistroLayout.createSequentialGroup()
                                .addGroup(pRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel3))
                                .addGap(33, 33, 33))
                            .addGroup(pRegistroLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(67, 67, 67)))
                        .addGroup(pRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cmbEscalonado, 0, 124, Short.MAX_VALUE)
                            .addComponent(cmbTecnico, javax.swing.GroupLayout.Alignment.LEADING, 0, 124, Short.MAX_VALUE)
                            .addComponent(cmbHoraInicio, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(64, 64, 64)
                        .addGroup(pRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4))
                        .addGap(70, 70, 70)
                        .addGroup(pRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cmbResolvio, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbHoraFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        pRegistroLayout.setVerticalGroup(
            pRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pRegistroLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(pRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbResolvio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(31, 31, 31)
                .addGroup(pRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(cmbHoraInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbHoraFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(pRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cmbEscalonado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(62, 62, 62)
                .addComponent(btnGuardar)
                .addContainerGap(110, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Registro", pRegistro);

        pRegistros.setBackground(new java.awt.Color(128, 230, 241));

        tblRegistros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblRegistros.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblRegistrosPropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(tblRegistros);

        btnImprimir.setText("Imprimir");
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        btnEliminar.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                btnEliminarPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout pRegistrosLayout = new javax.swing.GroupLayout(pRegistros);
        pRegistros.setLayout(pRegistrosLayout);
        pRegistrosLayout.setHorizontalGroup(
            pRegistrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pRegistrosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(pRegistrosLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(btnEliminar)
                .addGap(124, 124, 124)
                .addComponent(btnImprimir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pRegistrosLayout.setVerticalGroup(
            pRegistrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pRegistrosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pRegistrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnImprimir)
                    .addComponent(btnEliminar))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Registros", pRegistros);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbTecnicoPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_cmbTecnicoPropertyChange
    validarCampos();        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTecnicoPropertyChange

    private void cmbResolvioPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_cmbResolvioPropertyChange
validarCampos();          // TODO add your handling code here:
    }//GEN-LAST:event_cmbResolvioPropertyChange

    private void cmbEscalonadoPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_cmbEscalonadoPropertyChange
validarCampos();          // TODO add your handling code here:
    }//GEN-LAST:event_cmbEscalonadoPropertyChange

    private void btnGuardarPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_btnGuardarPropertyChange
validarCampos();          // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarPropertyChange

    private void cmbHoraInicioPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_cmbHoraInicioPropertyChange
validarCampos();        // TODO add your handling code here:
    }//GEN-LAST:event_cmbHoraInicioPropertyChange

    private void cmbHoraFinalPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_cmbHoraFinalPropertyChange
validarCampos();        // TODO add your handling code here:
    }//GEN-LAST:event_cmbHoraFinalPropertyChange

    private void cmbTecnicoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTecnicoItemStateChanged
        // TODO add your handling code here:
        validarCampos();
    }//GEN-LAST:event_cmbTecnicoItemStateChanged

    private void cmbResolvioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbResolvioItemStateChanged
       validarCampos();
    }//GEN-LAST:event_cmbResolvioItemStateChanged

    private void cmbHoraInicioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbHoraInicioItemStateChanged
validarCampos();        // TODO add your handling code here:
    }//GEN-LAST:event_cmbHoraInicioItemStateChanged

    private void cmbHoraFinalItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbHoraFinalItemStateChanged
validarCampos();        // TODO add your handling code here:
    }//GEN-LAST:event_cmbHoraFinalItemStateChanged

    private void cmbEscalonadoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbEscalonadoItemStateChanged
validarCampos();        // TODO add your handling code here:
    }//GEN-LAST:event_cmbEscalonadoItemStateChanged

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // Obtiene los valores seleccionados en los JComboBox
    String tecnico = (String) cmbTecnico.getSelectedItem();
    String resolvio = (String) cmbResolvio.getSelectedItem();
    String horaInicio = (String) cmbHoraInicio.getSelectedItem();
    String horaFinal = (String) cmbHoraFinal.getSelectedItem();
    String escalonado = (String) cmbEscalonado.getSelectedItem();

    // Llama al método guardarRegistro con los valores obtenidos
    guardarRegistro(tecnico, resolvio, horaInicio, horaFinal, escalonado);
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        try {
            generarPDF();
            JOptionPane.showMessageDialog(this, "PDF generado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al generar el PDF.", "Error", JOptionPane.ERROR_MESSAGE);
        }    }//GEN-LAST:event_btnImprimirActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int filaSeleccionada = tblRegistros.getSelectedRow();
    
    if (filaSeleccionada != -1) {
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar el registro seleccionado?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            // Obtener el valor de la columna "ID" en la fila seleccionada
            int idRegistro = (int) tblRegistros.getValueAt(filaSeleccionada, 0);
            
            // Llamar al método para eliminar el registro
            boolean eliminado = eliminarRegistro(idRegistro);
            
            if (eliminado) {
                JOptionPane.showMessageDialog(this, "Registro eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
                // Volver a seleccionar la fila después de la eliminación
                tblRegistros.setRowSelectionInterval(filaSeleccionada, filaSeleccionada);
                actualizarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el registro.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    } else {
        JOptionPane.showMessageDialog(this, "Seleccione un registro para eliminarlo.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void tblRegistrosPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblRegistrosPropertyChange
    selectTabla();        // TODO add your handling code here:
    }//GEN-LAST:event_tblRegistrosPropertyChange

    private void btnEliminarPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_btnEliminarPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarPropertyChange

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Panel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Panel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Panel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Panel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Panel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JToggleButton btnImprimir;
    private javax.swing.JComboBox<String> cmbEscalonado;
    private javax.swing.JComboBox<String> cmbHoraFinal;
    private javax.swing.JComboBox<String> cmbHoraInicio;
    private javax.swing.JComboBox<String> cmbResolvio;
    private javax.swing.JComboBox<String> cmbTecnico;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JPanel pRegistro;
    private javax.swing.JPanel pRegistros;
    private javax.swing.JTable tblRegistros;
    // End of variables declaration//GEN-END:variables

    private void generarPDF() throws FileNotFoundException{
         Connection connection = null;

        try {
            // Establecer la conexión con la base de datos
            String connectionString = "jdbc:sqlserver://ingsoftdatabase.database.windows.net:1433;database=IngSoftwareDB;user=admin26@ingsoftdatabase;password=@Axopunk2023.;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            connection = DriverManager.getConnection(connectionString);

            // Consulta SQL para obtener los datos de la tabla "Registros"
            String sql = "SELECT * FROM Registros";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Mostrar el diálogo de selección de archivo
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                // Crear el documento PDF en la ubicación seleccionada
                Document document = new Document(PageSize.A4);
                PdfWriter.getInstance(document, new FileOutputStream(selectedFile));
                document.open();

                // Agregar un título al PDF
                Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
                Paragraph title = new Paragraph("Reporte de Registros\n\n", titleFont);
                title.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(title);

                // Crear una tabla para los datos de la tabla "Registros"
                PdfPTable table = new PdfPTable(resultSet.getMetaData().getColumnCount());
                table.setWidthPercentage(100);

                // Agregar encabezados de columna a la tabla
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    PdfPCell headerCell = new PdfPCell(new Paragraph(resultSet.getMetaData().getColumnName(i)));
                    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(headerCell);
                }

                // Agregar datos de la tabla "Registros"
                while (resultSet.next()) {
                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                        PdfPCell cell;
                        if (resultSet.getMetaData().getColumnName(i).equals("Hr_Inicio") || resultSet.getMetaData().getColumnName(i).equals("Hr_Final")) {
                            // Formatear campos de hora inicio y hora final
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                            cell = new PdfPCell(new Paragraph(timeFormat.format(resultSet.getTime(i))));
                        } else {
                            cell = new PdfPCell(new Paragraph(resultSet.getString(i)));
                        }
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);
                    }
                }

                document.add(table);
                document.close();
            }
        } catch (DocumentException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al generar el PDF.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }}

    private boolean eliminarRegistro(int idRegistro) {
            String connectionString = "jdbc:sqlserver://ingsoftdatabase.database.windows.net:1433;database=IngSoftwareDB;user=admin26@ingsoftdatabase;password=@Axopunk2023.;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";

        String sql = "DELETE FROM Registros WHERE No_Orden = ?";
    try (Connection conn = DriverManager.getConnection(connectionString);
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idRegistro);
        int filasAfectadas = stmt.executeUpdate();
        return filasAfectadas > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
    }
}
