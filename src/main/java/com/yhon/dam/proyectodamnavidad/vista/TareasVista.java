/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.yhon.dam.proyectodamnavidad.vista;

import com.yhon.dam.proyectodamnavidad.MainFrame;
import com.yhon.dam.proyectodamnavidad.modelo.Usuario;
import javax.swing.table.DefaultTableModel;
import com.yhon.dam.proyectodamnavidad.modelo.Tarea;
import com.yhon.dam.proyectodamnavidad.dao.TareaDAO;
import com.yhon.dam.proyectodamnavidad.dao.UsuarioDAO;
import com.yhon.dam.proyectodamnavidad.util.I18n;
import com.yhon.dam.proyectodamnavidad.util.I18nAware;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 * Vista principal de gestión de tareas.
 * <p>
 * Muestra las tareas asignadas al usuario autenticado y permite:
 * </p>
 * <ul>
 * <li>Crear tareas</li>
 * <li>Finalizar tareas</li>
 * <li>Eliminar tareas finalizadas</li>
 * <li>Delegar tareas a otro usuario</li>
 * <li>Canjear puntos por recompensas</li>
 * </ul>
 *
 * Incluye soporte para deshacer la última finalización mediante la combinación
 * de teclas <b>Ctrl + Z</b>.
 *
 * Soporta internacionalización dinámica mediante {@link I18nAware}.
 *
 * @author yhon
 */
public class TareasVista extends javax.swing.JFrame implements I18nAware {

    private Long ultimaTareaFinalizadaId = null;
    private int puntosUltimaTarea = 0;

    /**
     * Usuario actualmente autenticado
     */
    private Usuario usuarioLogueado;

    /**
     * Modelo de la tabla de tareas
     */
    private DefaultTableModel modeloTabla;

    /**
     * Última tarea finalizada (para deshacer acción)
     */
    private Tarea ultimaTareaFinalizada = null;

    /**
     * Puntos otorgados por la última tarea finalizada
     */
    /**
     * Logger de la clase
     */
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TareasVista.class.getName());

    /**
     * Índice de la columna que almacena el objeto Tarea
     */
    private static final int COL_TAREA_OBJ = 0;

    /**
     * Índice de la columna del checkbox de finalización
     */
    private static final int COLUMNA_CHECKBOX = 8;

    /**
     * Crea la vista principal de tareas.
     *
     * @param usuarioLogueado usuario autenticado
     */
    public TareasVista(Usuario usuarioLogueado) {
        initComponents();
        this.usuarioLogueado = usuarioLogueado;

        inicializarTabla();
        cargarDatosUsuario();
        cargarTareas();
        configurarCtrlZ();
        aplicarIdioma();
    }

    /**
     * Inicializa la estructura y comportamiento de la tabla de tareas.
     * <p>
     * Se utiliza una columna oculta para almacenar el objeto {@link Tarea}
     * completo, facilitando operaciones posteriores sin romper el patrón MVC.
     * </p>
     */
    private void inicializarTabla() {
        tablaTareas.getColumnModel().getColumn(2).setPreferredWidth(300);
        tablaTareas.getColumnModel().getColumn(2).setMinWidth(250);

        modeloTabla = new DefaultTableModel(
                new Object[]{
                    "OBJ", // OCULTA
                    "Nombre",
                    "Descripción",
                    "Asignado por",
                    "Responsable",
                    "Fecha inicio",
                    "Fecha límite",
                    "Estado",
                    "Finalizar"
                }, 0
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == COLUMNA_CHECKBOX
                        ? Boolean.class
                        : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == COLUMNA_CHECKBOX;
            }
        };

        tablaTareas.setModel(modeloTabla);
        tablaTareas.getColumnModel().getColumn(0).setMinWidth(0);
        tablaTareas.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaTareas.getColumnModel().getColumn(0).setWidth(0);
        tablaTareas.getColumnModel().getColumn(COLUMNA_CHECKBOX).setMinWidth(0);
        tablaTareas.getColumnModel().getColumn(COLUMNA_CHECKBOX).setMaxWidth(0);
        tablaTareas.getColumnModel().getColumn(COLUMNA_CHECKBOX).setWidth(0);

    }

    /**
     * Carga y muestra los datos del usuario autenticado.
     */
    private void cargarDatosUsuario() {
        lblUsuario.setText(I18n.t("menu.user") + ": " + usuarioLogueado.getUsername()); // Traducir la palabra "Usuario"
        lblEmail.setText("Email: " + usuarioLogueado.getEmail());

        // Obtén el nombre del rol como String (usando name())
        String rolTraducido = I18n.getRolTranslation(usuarioLogueado.getRol().name());
        lblRol.setText(I18n.t("tasks.role") + ": " + rolTraducido); // Aquí se muestra el rol traducido

        lblPuntos.setText("Puntos: " + usuarioLogueado.getPuntos());
        btnQueOtro.setEnabled(usuarioLogueado.getPuntos() >= 1000);
    }

    /**
     * Recarga el usuario desde base de datos y actualiza la vista.
     */
    void refrescarUsuario() {
        usuarioLogueado = UsuarioDAO.buscarPorUsername(usuarioLogueado.getUsername());
        lblPuntos.setText(
                I18n.t("tasks.points") + " " + usuarioLogueado.getPuntos()
        );

        String rolTraducido = I18n.getRolTranslation(usuarioLogueado.getRol().name());

        lblRol.setText(
                I18n.t("tasks.role") + ": " + rolTraducido
        );

        btnQueOtro.setEnabled(usuarioLogueado.getPuntos() >= 1000);

    }

    /**
     * Permite seleccionar un nuevo responsable para delegar una tarea.
     *
     * @return usuario seleccionado o {@code null} si se cancela
     */
    private Usuario seleccionarNuevoResponsable() {

        List<Usuario> usuarios = UsuarioDAO.listarUsuarios();
        JComboBox<Usuario> combo = new JComboBox<>();

        for (Usuario u : usuarios) {
            if (!u.getId().equals(usuarioLogueado.getId())) {
                combo.addItem(u);
            }
        }

        Object[] options = {
            I18n.t("option.no"),
            I18n.t("option.yes")
        };

        int result = JOptionPane.showOptionDialog(
                this,
                combo,
                I18n.t("task.delegate.selectNew"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1] // YES por defecto
        );

        if (result != 1) { // 1 = YES
            return null;
        }

        return (Usuario) combo.getSelectedItem();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaTareas = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        btnFinalizar = new javax.swing.JButton();
        lblRol = new javax.swing.JLabel();
        lblPuntos = new javax.swing.JLabel();
        btnQueOtro = new javax.swing.JButton();
        canjearPuntosBtn = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        cerrarSesion = new javax.swing.JMenuItem();
        cambiarPass = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        espaniol = new javax.swing.JMenuItem();
        ingles = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        jLabel1.setText("TAREAS");

        lblUsuario.setText("jLabel3");

        lblEmail.setText("jLabel4");

        tablaTareas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Nombre", "Descricion", "Asignado por", "Responsable", "Fecha Inicio", "Fecha Limite", "Estado", "Finalizar"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaTareas.setToolTipText("");
        jScrollPane1.setViewportView(tablaTareas);

        jButton1.setText("Crear Tarea");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnFinalizar.setText("Finalizar Tarea");
        btnFinalizar.setToolTipText("");
        btnFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinalizarActionPerformed(evt);
            }
        });

        lblRol.setText("Rol:");

        lblPuntos.setText("Puntos:");

        btnQueOtro.setText("¡Que lo haga otro!");
        btnQueOtro.setToolTipText("Canjea puntos para asignar la tarea a otra persona");
        btnQueOtro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQueOtroActionPerformed(evt);
            }
        });

        canjearPuntosBtn.setText("Canjear Puntos");
        canjearPuntosBtn.setToolTipText("");
        canjearPuntosBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                canjearPuntosBtnActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar Tarea");
        btnEliminar.setToolTipText("");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/familiaTrabajando.png"))); // NOI18N

        jMenu4.setText("Sesion");
        jMenu4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu4ActionPerformed(evt);
            }
        });

        jMenuItem2.setText("Nuevo Usuario");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem2);

        cerrarSesion.setText("Cerrar Sesion");
        cerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cerrarSesionActionPerformed(evt);
            }
        });
        jMenu4.add(cerrarSesion);

        cambiarPass.setText("Cambiar Contraseña");
        cambiarPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiarPassActionPerformed(evt);
            }
        });
        jMenu4.add(cambiarPass);

        jMenuBar1.add(jMenu4);

        jMenu1.setText("Idioma");

        espaniol.setText("Español");
        espaniol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                espaniolActionPerformed(evt);
            }
        });
        jMenu1.add(espaniol);

        ingles.setText("Ingles");
        ingles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inglesActionPerformed(evt);
            }
        });
        jMenu1.add(ingles);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Ayuda");

        jMenuItem5.setText("Controles");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38)
                                .addComponent(lblRol, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(lblPuntos, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(121, 121, 121)
                                .addComponent(canjearPuntosBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnFinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnQueOtro, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRol)
                    .addComponent(lblPuntos)
                    .addComponent(lblUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(canjearPuntosBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(btnFinalizar)
                    .addComponent(btnQueOtro)
                    .addComponent(btnEliminar))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        CrearTareaVista crear = new CrearTareaVista(usuarioLogueado, this);
        crear.setLocationRelativeTo(this);
        crear.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinalizarActionPerformed
        // TODO add your handling code here:
        int filaSeleccionada = tablaTareas.getSelectedRow();

        Object[] okOption = {I18n.t("common.ok")};
        Object[] yesNoOptions = {
            I18n.t("option.no"),
            I18n.t("option.yes")
        };

// 1️⃣ No hay selección
        if (filaSeleccionada == -1) {

            JOptionPane.showOptionDialog(
                    this,
                    I18n.t("task.selectFirst"),
                    I18n.t("common.warning"),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    okOption,
                    okOption[0]
            );
            return;
        }

        Tarea tarea = (Tarea) modeloTabla.getValueAt(filaSeleccionada, COL_TAREA_OBJ);

// 2️⃣ Ya está finalizada
        if (Tarea.ESTADO_FINALIZADA.equalsIgnoreCase(tarea.getEstado())) {

            JOptionPane.showOptionDialog(
                    this,
                    I18n.t("task.alreadyFinished"),
                    I18n.t("common.info"),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    okOption,
                    okOption[0]
            );
            return;
        }

// 3️⃣ Confirmación
        int confirmacion = JOptionPane.showOptionDialog(
                this,
                I18n.t("task.finish.confirm"),
                I18n.t("task.finish.title"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                yesNoOptions,
                yesNoOptions[0]
        );

        if (confirmacion != 1) { // 1 = Yes
            return;
        }

// 4️⃣ Finalizar tarea
        // 4️⃣ Finalizar tarea
        TareaDAO.marcarFinalizada(tarea);
        tarea.setEstado(Tarea.ESTADO_FINALIZADA);
        modeloTabla.setValueAt(Tarea.ESTADO_FINALIZADA, filaSeleccionada, 7);

// 🔹 Gurdamos ultima tarea finalizada
        ultimaTareaFinalizadaId = tarea.getId();
        puntosUltimaTarea = tarea.getPuntosRecompensa();

        refrescarUsuario();

// 5️⃣ Éxito
        JOptionPane.showOptionDialog(
                this,
                I18n.t("task.finish.success"),
                I18n.t("common.success"),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                okOption,
                okOption[0]
        );

    }//GEN-LAST:event_btnFinalizarActionPerformed

    private void btnQueOtroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQueOtroActionPerformed
        int fila = tablaTareas.getSelectedRow();

        Object[] okOption = {I18n.t("common.ok")};
        Object[] yesNoOptions = {
            I18n.t("option.no"),
            I18n.t("option.yes")
        };

// 1️⃣ Selección
        if (fila == -1) {
            JOptionPane.showOptionDialog(
                    this,
                    I18n.t("task.select"),
                    I18n.t("common.warning"),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    okOption,
                    okOption[0]
            );
            return;
        }

        Tarea tarea = (Tarea) modeloTabla.getValueAt(fila, COL_TAREA_OBJ);

// 2️⃣ No permitir tareas finalizadas
        if (Tarea.ESTADO_FINALIZADA.equalsIgnoreCase(tarea.getEstado())) {
            JOptionPane.showOptionDialog(
                    this,
                    I18n.t("task.delegate.finished"),
                    I18n.t("common.denied"),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    okOption,
                    okOption[0]
            );
            return;
        }

// 3️⃣ Validar propiedad
        if (!tarea.getResponsable().getId().equals(usuarioLogueado.getId())) {
            JOptionPane.showOptionDialog(
                    this,
                    I18n.t("task.delegate.onlyOwn"),
                    I18n.t("common.error"),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    okOption,
                    okOption[0]
            );
            return;
        }

// 4️⃣ Elegir nuevo responsable
        Usuario nuevoResponsable = seleccionarNuevoResponsable();
        if (nuevoResponsable == null) {
            return;
        }

// 5️⃣ Confirmación
        int confirm = JOptionPane.showOptionDialog(
                this,
                I18n.t("task.delegate.confirm"),
                I18n.t("task.delegate.title"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                yesNoOptions,
                yesNoOptions[0]
        );

        if (confirm != 1) {
            return;
        }

// 6️⃣ Ejecutar
        boolean ok = TareaDAO.canjearQueLoHagaOtro(
                usuarioLogueado,
                tarea,
                nuevoResponsable
        );

// 7️⃣ Resultado
        if (ok) {
            JOptionPane.showOptionDialog(
                    this,
                    I18n.t("task.delegate.success"),
                    I18n.t("common.success"),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    okOption,
                    okOption[0]
            );
        } else {
            JOptionPane.showOptionDialog(
                    this,
                    I18n.t("task.delegate.error"),
                    I18n.t("common.error"),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    okOption,
                    okOption[0]
            );
        }


    }//GEN-LAST:event_btnQueOtroActionPerformed

    private void canjearPuntosBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_canjearPuntosBtnActionPerformed
        RecompensaVista vista = new RecompensaVista(usuarioLogueado, this);
        vista.setLocationRelativeTo(this);
        vista.setVisible(true);       // TODO add your handling code here:
    }//GEN-LAST:event_canjearPuntosBtnActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int fila = tablaTareas.getSelectedRow();

        Object[] okOption = {I18n.t("common.ok")};
        Object[] yesNoOptions = {
            I18n.t("option.no"),
            I18n.t("option.yes")
        };

        if (fila == -1) {

            JOptionPane.showOptionDialog(
                    this,
                    I18n.t("task.delete.select"),
                    I18n.t("common.warning"),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    okOption,
                    okOption[0]
            );
            return;
        }

        Tarea tarea = (Tarea) modeloTabla.getValueAt(fila, COL_TAREA_OBJ);

// 🔒 Solo permitir eliminar si está finalizada
        if (!"FINALIZADA".equals(tarea.getEstado())) {

            JOptionPane.showOptionDialog(
                    this,
                    I18n.t("task.delete.onlyFinished"),
                    I18n.t("task.delete.notAllowed"),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    okOption,
                    okOption[0]
            );
            return;
        }

// Confirmación
        int confirm = JOptionPane.showOptionDialog(
                this,
                I18n.t("task.delete.confirmMessage"),
                I18n.t("task.delete.confirmTitle"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                yesNoOptions,
                yesNoOptions[0]
        );

        if (confirm != 1) { // 1 = Sí
            return;
        }

        boolean ok = TareaDAO.eliminar(tarea);

        if (ok) {

            JOptionPane.showOptionDialog(
                    this,
                    I18n.t("task.delete.success"),
                    I18n.t("common.success"),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    okOption,
                    okOption[0]
            );

            cargarTareas();

        } else {

            JOptionPane.showOptionDialog(
                    this,
                    I18n.t("task.delete.error"),
                    I18n.t("common.error"),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    okOption,
                    okOption[0]
            );
        }


    }//GEN-LAST:event_btnEliminarActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        RegistroVista registro = new RegistroVista();
        registro.setLocationRelativeTo(this);
        registro.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenu4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu4ActionPerformed
        // TODO add your handling code here:
        RegistroVista registro = new RegistroVista();
        registro.setLocationRelativeTo(this); // centrado respecto al login
        registro.setVisible(true);
    }//GEN-LAST:event_jMenu4ActionPerformed

    private void espaniolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_espaniolActionPerformed
        // TODO add your handling code here:
        I18n.setLanguage("es");
        aplicarIdioma();
    }//GEN-LAST:event_espaniolActionPerformed

    private void inglesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inglesActionPerformed
        // TODO add your handling code here:
        I18n.setLanguage("en");
        aplicarIdioma();
    }//GEN-LAST:event_inglesActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        Object[] options = {I18n.t("common.ok")};

        JOptionPane.showOptionDialog(
                this,
                I18n.t("help.content"),
                I18n.t("help.title"),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void cerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cerrarSesionActionPerformed

        // Botones traducidos
        Object[] options = {
            I18n.t("option.no"),
            I18n.t("option.yes")
        };

        int confirm = JOptionPane.showOptionDialog(
                this,
                I18n.t("menu.logout") + "?",
                I18n.t("menu.sesion"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (confirm != 1) { // 1 = Sí
            return;
        }

        this.dispose(); // Cierra la ventana actual

        MainFrame login = new MainFrame();
        login.setLocationRelativeTo(null); // Centrar
        login.setVisible(true);


    }//GEN-LAST:event_cerrarSesionActionPerformed

    private void cambiarPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiarPassActionPerformed
        // TODO add your handling code here:
        CambiarContrasenaVista vista
                = new CambiarContrasenaVista(usuarioLogueado);

        vista.setLocationRelativeTo(this); // centrada respecto a la ventana actual
        vista.setVisible(true);
    }//GEN-LAST:event_cambiarPassActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnFinalizar;
    private javax.swing.JButton btnQueOtro;
    private javax.swing.JMenuItem cambiarPass;
    private javax.swing.JButton canjearPuntosBtn;
    private javax.swing.JMenuItem cerrarSesion;
    private javax.swing.JMenuItem espaniol;
    private javax.swing.JMenuItem ingles;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblPuntos;
    private javax.swing.JLabel lblRol;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JTable tablaTareas;
    // End of variables declaration//GEN-END:variables

    /**
     * Carga las tareas asignadas al usuario autenticado.
     */
    void cargarTareas() {

        List<Tarea> tareas = TareaDAO.listarPorResponsable(usuarioLogueado);

        modeloTabla.setRowCount(0);

        for (Tarea t : tareas) {
            modeloTabla.addRow(new Object[]{
                t, // 👈 objeto completo
                t.getNombre(),
                t.getDescripcion(),
                t.getAsignadoPor().getUsername(),
                t.getResponsable().getUsername(),
                t.getFechaInicio(),
                t.getFechaLimite(),
                t.getEstado(),
                false
            });
        }
    }

    /**
     * Configura la combinación de teclas Ctrl + Z para deshacer la última tarea
     * finalizada.
     */
    private void configurarCtrlZ() {

        KeyStroke ctrlZ = KeyStroke.getKeyStroke(
                KeyEvent.VK_Z,
                InputEvent.CTRL_DOWN_MASK
        );

        tablaTareas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(ctrlZ, "deshacer");

        tablaTareas.getActionMap().put("deshacer", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deshacerUltimaAccion();
            }
        });
    }

    /**
     * Deshace la última acción de finalización de tarea.
     */
    private void deshacerUltimaAccion() {

        if (ultimaTareaFinalizadaId == null) {
            JOptionPane.showMessageDialog(
                    this,
                    I18n.t("task.undo.none"),
                    I18n.t("common.info"),
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                I18n.t("task.undo.confirm"),
                I18n.t("task.undo.title"),
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        boolean ok = TareaDAO.deshacerFinalizacion(
                ultimaTareaFinalizadaId,
                puntosUltimaTarea
        );

        if (ok) {
            ultimaTareaFinalizadaId = null;
            puntosUltimaTarea = 0;

            cargarTareas();
            refrescarUsuario();
        }
    }

    /**
     * Aplica los textos traducidos según el idioma actual.
     */
    @Override
    public void aplicarIdioma() {
        setTitle(I18n.t("tasks.title"));
        cambiarPass.setText(I18n.t("menu.changePassword"));
        lblUsuario.setText(
                I18n.t("menu.user") + ": " + usuarioLogueado.getUsername()
        );
        tablaTareas.setToolTipText(I18n.t("tooltip.undo"));
        btnQueOtro.setToolTipText(I18n.t("tooltip.delegate"));
        btnEliminar.setToolTipText(I18n.t("tooltip.delete"));
        canjearPuntosBtn.setToolTipText(I18n.t("tooltip.rewards"));
        String rolTraducido = I18n.getRolTranslation(usuarioLogueado.getRol().name());
        lblRol.setText(I18n.t("tasks.role") + ": " + rolTraducido);

        jLabel1.setText(I18n.t("tasks.header"));
        jButton1.setText(I18n.t("task.create.title"));
        btnFinalizar.setText(I18n.t("tasks.finish"));
        btnEliminar.setText(I18n.t("tasks.delete"));
        btnQueOtro.setText(I18n.t("tasks.delegate"));
        canjearPuntosBtn.setText(I18n.t("tasks.rewards"));

        lblPuntos.setText(
                I18n.t("tasks.points") + ": " + usuarioLogueado.getPuntos()
        );

        // Cabecera tabla
        tablaTareas.getColumnModel().getColumn(1)
                .setHeaderValue(I18n.t("task.name"));
        tablaTareas.getColumnModel().getColumn(2)
                .setHeaderValue(I18n.t("task.description"));
        tablaTareas.getColumnModel().getColumn(3)
                .setHeaderValue(I18n.t("task.assignedBy"));
        tablaTareas.getColumnModel().getColumn(4)
                .setHeaderValue(I18n.t("task.responsible"));
        tablaTareas.getColumnModel().getColumn(5)
                .setHeaderValue(I18n.t("task.startDate"));
        tablaTareas.getColumnModel().getColumn(6)
                .setHeaderValue(I18n.t("task.endDate"));
        tablaTareas.getColumnModel().getColumn(7)
                .setHeaderValue(I18n.t("task.status"));
        tablaTareas.getColumnModel().getColumn(8)
                .setHeaderValue(I18n.t("task.finishColumn"));

        tablaTareas.getTableHeader().repaint();

        jMenu4.setText(I18n.t("menu.sesion"));
        jMenuItem2.setText(I18n.t("menu.newuser"));
        cerrarSesion.setText(I18n.t("menu.logout"));
        espaniol.setText(I18n.t("menu.spanish"));
        ingles.setText(I18n.t("menu.english"));
        jMenu2.setText(I18n.t("menu.help"));
        jMenuItem5.setText(I18n.t("menu.controls"));
        jMenu1.setText(I18n.t("menu.lenguage"));

    }

}
