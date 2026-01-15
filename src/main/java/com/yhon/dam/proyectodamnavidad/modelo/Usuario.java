/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.yhon.dam.proyectodamnavidad.modelo;

import jakarta.persistence.*;

/**
 * Entidad que representa un usuario del sistema.
 * <p>
 * Cada usuario dispone de credenciales de acceso, un rol
 * que define sus permisos y un sistema de puntos asociado
 * a la realización de tareas.
 * </p>
 *
 * Está mapeada a la tabla {@code usuarios} mediante JPA.
 *
 * @author yhon
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    /**
     * Identificador único del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario.
     * <p>
     * Debe ser único dentro del sistema.
     * </p>
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * Hash de la contraseña del usuario.
     * <p>
     * Nunca se almacena la contraseña en texto plano.
     * </p>
     */
    @Column(nullable = false)
    private String passwordHash;

    /**
     * Correo electrónico del usuario.
     */
    private String email;

    /**
     * Rol del usuario dentro del sistema.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    /**
     * Puntos acumulados por el usuario.
     */
    @Column(nullable = false)
    private int puntos;

    /**
     * Devuelve el identificador del usuario.
     *
     * @return id del usuario
     */
    public Long getId() {
        return id;
    }

    /**
     * Devuelve el nombre de usuario.
     *
     * @return nombre de usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario.
     *
     * @param username nombre de usuario
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Devuelve el hash de la contraseña del usuario.
     *
     * @return hash de la contraseña
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Establece el hash de la contraseña del usuario.
     *
     * @param passwordHash hash de la contraseña
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Devuelve el correo electrónico del usuario.
     *
     * @return correo electrónico
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     *
     * @param email correo electrónico
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Devuelve el rol del usuario.
     *
     * @return rol del usuario
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * Establece el rol del usuario.
     *
     * @param rol rol del usuario
     */
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    /**
     * Devuelve los puntos acumulados por el usuario.
     *
     * @return puntos del usuario
     */
    public int getPuntos() {
        return puntos;
    }

    /**
     * Establece los puntos del usuario.
     *
     * @param puntos puntos a establecer
     */
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    /**
     * Representación textual del usuario.
     * <p>
     * Se utiliza principalmente en componentes gráficos
     * como listas y JComboBox.
     * </p>
     *
     * @return nombre de usuario
     */
    @Override
    public String toString() {
        return username;
    }
}
