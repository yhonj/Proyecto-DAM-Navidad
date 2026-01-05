/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.yhon.dam.proyectodamnavidad.modelo;

/**
 *
 * @author yhon
 */
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tareas")
public class Tarea {
private static final int COL_TAREA_OBJ = 0;
private static final int COLUMNA_CHECKBOX = 8;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "asignado_por", nullable = false)
    private Usuario asignadoPor;

    @ManyToOne
    @JoinColumn(name = "responsable", nullable = false)
    private Usuario responsable;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_limite")
    private LocalDate fechaLimite;

    @Column(nullable = false)
    private String estado;

    // 🔹 CONSTRUCTOR VACÍO (OBLIGATORIO PARA HIBERNATE)
    public Tarea() {
        this.estado = "PENDIENTE";
    }

    // 🔹 CONSTRUCTOR ÚTIL
    public Tarea(String nombre, String descripcion, Usuario asignadoPor,
                 Usuario responsable, LocalDate fechaInicio, LocalDate fechaLimite) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.asignadoPor = asignadoPor;
        this.responsable = responsable;
        this.fechaInicio = fechaInicio;
        this.fechaLimite = fechaLimite;
        this.estado = "PENDIENTE";
    }

    // 🔹 GETTERS Y SETTERS

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Usuario getAsignadoPor() {
        return asignadoPor;
    }

    public void setAsignadoPor(Usuario asignadoPor) {
        this.asignadoPor = asignadoPor;
    }

    public Usuario getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuario responsable) {
        this.responsable = responsable;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
