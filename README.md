# Conflict Tracker (proyecto)

## Descricpcion
Aplicacion que se basa en el seguimiento de conflictos activos en el mundo

Los datos de la aplicacion se resetean/cambian cada vez que se reinicia la aplicacion

## Ejecucion
Ejecuta el programa desde COnflictTrackerApplication o comn el comando

./gradlew bootRun

LA aplicacion se abrira en la url:
http://localhost:8080

## Acceso a la base de datos
La aplicación utiliza una base de datos en memoria. Para ver y manipular los datos directamente:
Abre tu navegador y ve a: http://localhost:8080/h2-console
IMPORTANTE: Configura los datos de conexión exactamente así (basado en tu application.yaml):
Driver Class: org.h2.Driver
JDBC URL: jdbc:h2:mem:conflicttrackerdb
User Name: ifaldmo
Password: ifaldmo

A mime ha dado un error porque no se porque se abria la consola con el JDBC id cambiado, hayq ue asegurarse de que eso este bien puesto

## COMANDOS

Para crear un conflicto utilizando la h2 console:

INSERT INTO conflict (name, start_date, status, description)
VALUES ('Conflicto Manual', '2025-01-01', 'ACTIVE', 'Insertado desde la consola H2');

PAra actualizarlo:

UPDATE conflict
SET name = 'Nombre Actualizado', status = 'RESOLVED', description = 'El conflicto ha terminado'
SET name = 'Nombre Actualizado', status = 'FROZEN', description = 'El conflicto ha terminado'
WHERE id = 1;

Para borrar un conflicto:

DELETE FROM conflict WHERE id = 1;

## FRONTEND
Para acceder al frontend como vista (sin poder hacer nada), vamos a http://localhost:8080/index.html
Si se quiere añadir o eliminar conflictos, se va a http://localhost:8080/web/conflicts

# AL UTILIZAR LOS 2, TODO SE SINCRONIZA SIN PROBLEMAS