/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.yhon.dam.proyectodamnavidad.dao;

import com.yhon.dam.proyectodamnavidad.modelo.Recompensa;
import com.yhon.dam.proyectodamnavidad.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;

/**
 * DAO (Data Access Object) encargado de la gestión de recompensas.
 * <p>
 * Proporciona métodos para acceder a las recompensas almacenadas
 * en la base de datos utilizando Hibernate.
 * </p>
 *
 * @author yhon
 */
public class RecompensaDAO {

    /**
     * Obtiene la lista completa de recompensas disponibles.
     * <p>
     * Recupera todas las entidades {@link Recompensa} almacenadas
     * en la base de datos.
     * </p>
     *
     * @return lista de recompensas
     */
    public static List<Recompensa> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM Recompensa",
                Recompensa.class
            ).getResultList();
        }
    }
}
