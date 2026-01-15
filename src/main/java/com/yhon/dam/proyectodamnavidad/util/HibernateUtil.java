package com.yhon.dam.proyectodamnavidad.util;

import com.yhon.dam.proyectodamnavidad.modelo.Recompensa;
import com.yhon.dam.proyectodamnavidad.modelo.Tarea;
import com.yhon.dam.proyectodamnavidad.modelo.Usuario;
import java.sql.Connection;
import java.sql.DriverManager;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Utilidad para la gestión de Hibernate.
 * <p>
 * Esta clase se encarga de crear y proporcionar una única instancia
 * de {@link SessionFactory} para toda la aplicación.
 * </p>
 *
 * <p>
 * Detecta automáticamente si MySQL está disponible en el sistema.
 * En caso contrario, utiliza una base de datos H2 embebida como
 * alternativa, lo que facilita el desarrollo y las pruebas.
 * </p>
 *
 * Implementa el patrón Singleton de forma sencilla para una
 * aplicación de escritorio.
 *
 * @author yhon
 */
public class HibernateUtil {

    /**
     * Única instancia de SessionFactory de la aplicación.
     */
    private static SessionFactory sessionFactory;

    /**
     * Devuelve la {@link SessionFactory} de Hibernate.
     * <p>
     * Si no existe todavía, la crea configurando automáticamente
     * el origen de datos (MySQL o H2) y registrando las entidades
     * anotadas del sistema.
     * </p>
     *
     * @return instancia única de SessionFactory
     * @throws ExceptionInInitializerError si ocurre un error al inicializar Hibernate
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration cfg = new Configuration();

                if (mysqlDisponible()) {
                    System.out.println("MySQL detectado");
                    cfg.configure("hibernate.cfg.xml");
                } else {
                    System.out.println("MySQL no disponible → usando H2");
                    cfg.configure("hibernate-h2.cfg.xml");
                }

                // Registro explícito de entidades anotadas
                cfg.addAnnotatedClass(Usuario.class);
                cfg.addAnnotatedClass(Tarea.class);
                cfg.addAnnotatedClass(Recompensa.class);

                sessionFactory = cfg.buildSessionFactory();

            } catch (Throwable ex) {
                System.err.println("❌ Error creando SessionFactory");
                ex.printStackTrace();
                throw new ExceptionInInitializerError(ex);
            }
        }
        return sessionFactory;
    }

    /**
     * Comprueba si una base de datos MySQL está disponible.
     * <p>
     * Intenta cargar el driver de MySQL y establecer una conexión
     * con la base de datos configurada.
     * </p>
     *
     * @return {@code true} si MySQL está disponible, {@code false} en caso contrario
     */
    private static boolean mysqlDisponible() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/proyecto_dam",
                    "root",
                    "root"
            );
            conn.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
