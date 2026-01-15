/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.yhon.dam.proyectodamnavidad.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entidad que representa una tarea del sistema.
 * <p>
 * Las tareas son asignadas a un responsable y pueden otorgar
 * puntos al ser finalizadas. Cada tarea tiene un estado que
 * indica su progreso dentro del sistema.
 * </p>
 *
 * Está mapeada a la tabla {@code tareas} mediante JPA.
 *
 * @author yhon
 */
@Entity
@Table(name = "tareas")
public class Tarea {

    /**
     * Estado que indica que la tarea ha sido finalizada.
     */
    public static final String ESTADO_FINALIZADA = "FINALIZADA";

    /**
     * Índice de columna que representa el objeto tarea en tablas gráficas.
     * <p>
     * Utilizado en la capa de vista.
     * </p>
     */
    private static final int COL_TAREA_OBJ = 0;

    /**
     * Índice de columna del checkbox en tablas gráficas.
     */
    private static final int COLUMNA_CHECKBOX = 8;

    /**
     * Identificador único de la tarea.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de la tarea.
     */
    @Column(nullable = false)
    private String nombre;

    /**
     * Descripción detallada de la tarea.
     */
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    /**
     * Usuario que asigna la tarea.
     */
    @ManyToOne
    @JoinColumn(name = "asignado_por", nullable = false)
    private Usuario asignadoPor;

    /**
     * Usuario responsable de realizar la tarea.
     */
    @ManyToOne
    @JoinColumn(name = "responsable", nullable = false)
    private Usuario responsable;

    /**
     * Fecha de inicio de la tarea.
     */
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    /**
     * Fecha límite para completar la tarea.
     */
    @Column(name = "fecha_limite")
    private LocalDate fechaLimite;

    /**
     * Estado actual de la tarea.
     */
    @Column(nullable = false)
    private String estado;

    /**
     * Puntos que se otorgan al completar la tarea.
     */
    @Column(nullable = false)
    private int puntosRecompensa;

    /**
     * Constructor vacío obligatorio para Hibernate.
     * <p>
     * Inicializa el estado de la tarea como pendiente.
     * </p>
     */
    public Tarea() {
        this.estado = "PENDIENTE";
    }

    /**
     * Crea una tarea con los datos principales.
     *
     * @param nombre nombre de la tarea
     * @param descripcion descripción de la tarea
     * @param asignadoPor usuario que asigna la tarea
     * @param responsable usuario responsable de la tarea
     * @param fechaInicio fecha de inicio
     * @param fechaLimite fecha límite
     */
    public Tarea(
            String nombre,
            String descripcion,
            Usuario asignadoPor,
            Usuario responsable,
            LocalDate fechaInicio,
            LocalDate fechaLimite
    ) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.asignadoPor = asignadoPor;
        this.responsable = responsable;
        this.fechaInicio = fechaInicio;
        this.fechaLimite = fechaLimite;
        this.estado = "PENDIENTE";
    }

    /**
     * Devuelve el identificador de la tarea.
     *
     * @return id de la tarea
     */
    public Long getId() {
        return id;
    }

    /**
     * Devuelve el nombre de la tarea.
     *
     * @return nombre de la tarea
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la tarea.
     *
     * @param nombre nombre de la tarea
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve la descripción de la tarea.
     *
     * @return descripción de la tarea
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción de la tarea.
     *
     * @param descripcion descripción de la tarea
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve los puntos de recompensa de la tarea.
     *
     * @return puntos de recompensa
     */
    public int getPuntosRecompensa() {
        return puntosRecompensa;
    }

    /**
     * Establece los puntos de recompensa de la tarea.
     *
     * @param puntosRecompensa puntos a otorgar
     */
    public void setPuntosRecompensa(int puntosRecompensa) {
        this.puntosRecompensa = puntosRecompensa;
    }

    /**
     * Devuelve el usuario que asigna la tarea.
     *
     * @return usuario asignador
     */
    public Usuario getAsignadoPor() {
        return asignadoPor;
    }

    /**
     * Establece el usuario que asigna la tarea.
     *
     * @param asignadoPor usuario asignador
     */
    public void setAsignadoPor(Usuario asignadoPor) {
        this.asignadoPor = asignadoPor;
    }

    /**
     * Devuelve el usuario responsable de la tarea.
     *
     * @return usuario responsable
     */
    public Usuario getResponsable() {
        return responsable;
    }

    /**
     * Establece el usuario responsable de la tarea.
     *
     * @param responsable usuario responsable
     */
    public void setResponsable(Usuario responsable) {
        this.responsable = responsable;
    }

    /**
     * Devuelve la fecha de inicio de la tarea.
     *
     * @return fecha de inicio
     */
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Establece la fecha de inicio de la tarea.
     *
     * @param fechaInicio fecha de inicio
     */
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Devuelve la fecha límite de la tarea.
     *
     * @return fecha límite
     */
    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    /**
     * Establece la fecha límite de la tarea.
     *
     * @param fechaLimite fecha límite
     */
    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    /**
     * Devuelve el estado actual de la tarea.
     *
     * @return estado de la tarea
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado de la tarea.
     *
     * @param estado nuevo estado de la tarea
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
}
