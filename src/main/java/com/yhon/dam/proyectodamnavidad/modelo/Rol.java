/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.yhon.dam.proyectodamnavidad.modelo;

/**
 * Enumeración que define los distintos roles de usuario del sistema.
 * <p>
 * Los roles determinan los permisos y responsabilidades de cada
 * usuario dentro de la aplicación de gestión de tareas familiares.
 * </p>
 *
 * Se almacena en la base de datos como texto mediante
 * {@code EnumType.STRING}.
 *
 * @author yhon
 */
public enum Rol {

    /** Usuario con rol de padre */
    PADRE,

    /** Usuario con rol de madre */
    MADRE,

    /** Usuario con rol de hijo */
    HIJO,

    /** Usuario con rol de nieto */
    NIETO,

    /** Usuario con rol de sobrino */
    SOBRINO,

    /** Usuario con rol de tío */
    TIO
}
