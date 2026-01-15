Manual de la aplicación
Gestor de Tareas con Sistema de Recompensas
1. Introducción

Este proyecto consiste en una aplicación de escritorio desarrollada en Java cuyo objetivo es la gestión de tareas asociadas a un sistema de puntos y recompensas.
La aplicación permite a los usuarios crear, asignar y finalizar tareas, obteniendo puntos que posteriormente pueden canjear por recompensas predefinidas.

El programa ha sido desarrollado como proyecto académico, aplicando conceptos de programación orientada a objetos, persistencia de datos, arquitectura por capas y gestión de bases de datos, con la intención de simular un entorno real de aplicación.

2. Objetivo del programa

El objetivo principal del programa es:

Facilitar la organización de tareas

Motivar a los usuarios mediante un sistema de recompensas

Gestionar usuarios con distintos roles

Practicar el uso de Hibernate, bases de datos y contenedores Docker

La aplicación está pensada especialmente para:

Entornos familiares

Uso educativo

Aprendizaje de gestión de tareas

Proyectos académicos de desarrollo de aplicaciones

3. Público al que va dirigido

El programa va dirigido a:

Usuarios domésticos que quieran organizar tareas

Estudiantes y profesores

Personas que necesiten un sistema simple de tareas con incentivos

Desarrolladores en formación que quieran estudiar un ejemplo completo en Java

No requiere conocimientos técnicos para su uso normal.

4. Tecnologías utilizadas

La aplicación ha sido desarrollada utilizando las siguientes tecnologías:

Java SE (JDK 17 o superior)

Swing para la interfaz gráfica

Hibernate ORM para la persistencia de datos

JPA (Jakarta Persistence)

MySQL 8 como base de datos principal

H2 como base de datos alternativa local

Docker y Docker Compose para despliegue con contenedores

Maven para la gestión del proyecto

5. Arquitectura del programa

La aplicación sigue una arquitectura por capas:

Modelo: Entidades JPA (Usuario, Tarea, Recompensa)

DAO: Acceso a datos y operaciones con Hibernate

Controlador: Lógica de negocio

Vista: Interfaz gráfica con Swing

Utilidades: Gestión de Hibernate y detección de base de datos

Esta separación facilita el mantenimiento, la ampliación y la comprensión del código.

6. Requisitos del sistema
Requisitos mínimos

Sistema operativo Windows, Linux o macOS

Java JDK 17 o superior

512 MB de RAM

200 MB de espacio en disco

Requisitos adicionales (opcional)

Docker

Docker Compose

7. Funcionamiento general del programa

Al iniciar la aplicación:

Se muestra la ventana de login

El usuario inicia sesión

Se accede a la pantalla principal de tareas

El usuario puede:

Crear tareas

Finalizarlas

Reasignarlas

Acumular puntos

Canjear recompensas

Se puede cerrar sesión sin cerrar el programa

8. Ejecución del programa SIN Docker (modo local)

Este modo no requiere ninguna configuración adicional.

8.1 Funcionamiento

Si MySQL no está disponible, el programa utiliza automáticamente H2

La base de datos se crea localmente en el directorio data/

Las tablas se generan automáticamente

El programa es totalmente funcional

8.2 Ejecución

Desde la carpeta donde esté el archivo .jar:

java -jar ProyectoDAMNavidad-1.0-SNAPSHOT.jar


Este modo es ideal para pruebas rápidas o usuarios sin conocimientos técnicos.

9. Ejecución del programa CON Docker y MySQL

Este modo permite usar una base de datos MySQL real, persistente y compartida.

9.1 Estructura necesaria
ProyectoDAM/
│
├── docker-compose.yml
├── init.sql
└── ProyectoDAMNavidad-1.0-SNAPSHOT.jar

9.2 Archivo docker-compose.yml
version: "3.9"

services:
  mysql:
    image: mysql:8.0
    container_name: mysql_proyecto_dam
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: proyecto_dam
      MYSQL_USER: root
      MYSQL_PASSWORD: root
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
      - ./init.sql:/docker-entrypoint-initdb.d/init.sql

volumes:
  mysql_data:

9.3 Archivo init.sql

Este archivo crea las tablas y carga las recompensas iniciales.

Contiene:

Estructura de la base de datos

Recompensas predefinidas listas para usar

Se ejecuta automáticamente la primera vez que se crea el contenedor.

9.4 Arranque del contenedor
docker compose up -d


Para comprobar que está activo:

docker ps

9.5 Ejecución del programa con Docker activo
java -jar ProyectoDAMNavidad-1.0-SNAPSHOT.jar


La aplicación:

Detecta MySQL

Se conecta automáticamente

Usa los datos ya cargados

10. Cierre del sistema

Detener Docker sin borrar datos:

docker compose down


Borrar completamente la base de datos:

docker compose down -v

11. Conclusión

Esta aplicación demuestra el uso práctico de Java junto con Hibernate y bases de datos, incorporando además Docker como sistema de despliegue.
El programa es flexible, ya que puede funcionar tanto en modo local como con una base de datos completa en MySQL, lo que facilita su uso en distintos entornos y su distribución.
