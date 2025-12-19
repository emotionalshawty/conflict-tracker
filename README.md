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
SET name = 'Nombre Actualizado', status = 'FROZEN', description = 'El conflicto ha terminado'
WHERE id = 1;

Para borrar un conflicto:

DELETE FROM conflict WHERE id = 1;
## NO HAY MAS COMMITS PORQUE DURANTE UN BUEN RATO WINDOWS EN SI ME IBA MUY LOCOM, HE PUESTO CAPTURA DE COMO SE ME ESTABA REINSTALANDO HASTA HACE MUY POCO WINDOWS EN SI PORQUE ESTABA CORROMPIDO (REPAIR VERSION)