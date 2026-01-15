package com.yhon.dam.proyectodamnavidad.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad que representa una recompensa del sistema.
 * <p>
 * Las recompensas pueden ser canjeadas por los usuarios
 * utilizando los puntos obtenidos al completar tareas.
 * </p>
 *
 * Está mapeada a la tabla {@code recompensas} mediante JPA.
 *
 * @author yhon
 */
@Entity
@Table(name = "recompensas")
public class Recompensa {

    /**
     * Identificador único de la recompensa.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de la recompensa.
     */
    @Column(nullable = false)
    private String nombre;

    /**
     * Coste en puntos necesario para canjear la recompensa.
     */
    @Column(name = "coste_puntos", nullable = false)
    private int costePuntos;

    /**
     * Descripción opcional de la recompensa.
     */
    private String descripcion;

    /**
     * Constructor vacío requerido por Hibernate.
     */
    public Recompensa() {
    }

    /**
     * Crea una recompensa con nombre y coste en puntos.
     *
     * @param nombre nombre de la recompensa
     * @param costePuntos coste en puntos
     */
    public Recompensa(String nombre, int costePuntos) {
        this.nombre = nombre;
        this.costePuntos = costePuntos;
    }

    /**
     * Crea una recompensa con nombre, coste y descripción.
     *
     * @param nombre nombre de la recompensa
     * @param costePuntos coste en puntos
     * @param descripcion descripción de la recompensa
     */
    public Recompensa(String nombre, int costePuntos, String descripcion) {
        this.nombre = nombre;
        this.costePuntos = costePuntos;
        this.descripcion = descripcion;
    }

    /**
     * Devuelve el identificador de la recompensa.
     *
     * @return id de la recompensa
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador de la recompensa.
     *
     * @param id identificador
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre de la recompensa.
     *
     * @return nombre de la recompensa
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la recompensa.
     *
     * @param nombre nombre de la recompensa
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve el coste en puntos de la recompensa.
     *
     * @return coste en puntos
     */
    public int getCostePuntos() {
        return costePuntos;
    }

    /**
     * Establece el coste en puntos de la recompensa.
     *
     * @param costePuntos coste en puntos
     */
    public void setCostePuntos(int costePuntos) {
        this.costePuntos = costePuntos;
    }

    /**
     * Devuelve la descripción de la recompensa.
     *
     * @return descripción de la recompensa
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción de la recompensa.
     *
     * @param descripcion descripción de la recompensa
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Representación textual de la recompensa.
     * <p>
     * Se utiliza principalmente para depuración y registros.
     * </p>
     *
     * @return representación en forma de texto
     */
    @Override
    public String toString() {
        return "Recompensa{" +
               "id=" + id +
               ", nombre=" + nombre +
               ", costePuntos=" + costePuntos +
               ", descripcion=" + descripcion +
               '}';
    }
}
