/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.yhon.dam.proyectodamnavidad.util;

/**
 * Interfaz para componentes que soportan internacionalización dinámica.
 * <p>
 * Las clases que implementan esta interfaz se comprometen a actualizar
 * sus textos visibles cuando cambia el idioma de la aplicación.
 * </p>
 *
 * Se utiliza principalmente en ventanas y vistas que necesitan
 * refrescar etiquetas, botones y menús tras un cambio de idioma.
 *
 * @author yhon
 */
public interface I18nAware {

    /**
     * Aplica el idioma actual a los textos del componente.
     * <p>
     * Este método debe actualizar todos los textos visibles
     * utilizando la utilidad {@link I18n}.
     * </p>
     */
    void aplicarIdioma();
}
