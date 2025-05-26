# P2-Sistemas-Distribuidos
Taller de la asignatura de Sistemas Distribuidos. Consiste en una aplicación que modela un sistema de gestión de eventos, estos eventos y las actividades de los mismos pueden ser creados y gestionados por el organizador, también se pueden agregar monitores para las diferentes actividades del evento. Los participantes pueden apuntarse a estos y ver las actividades de los eventos, así como información sobre la ubicación, descripción, horarios, etc. 

Se incluye un modo administrador con el que poder gestionar además los usuarios del sistema.

## Características técnicas
### Spring Boot
La aplicación consisten principalmente en un servidor web con Spring Boot, cuenta con las siguientes características:
- Genera las vistas con Thymeleaf.
- Soporta login securizado.
- Aporta un sistema de gestión de usuarios.
- Base de datos MySQL.
- Uso de la API de Leaflet (alternativo a Google Maps).

### MySQL
- Contiene una base de datos llamada "taller".
- Tabla users:
    - Contiene información de idenficación del usuario
    - Gestiona los roles y niveles de acceso del los usuarios
- Tabla de eventos:
    - Contiene información como descripción, duración...
    - Contiene referencias al usuario organizador
- Tabla de actividades:
    - Contiene la información de la actividad
    - Hace referencia al monitor que imparte la actividad
    - Cotiene una referencia al taller donde se imparte

## Uso
Para generar los ejecutables y poner en marcha los contenedores de Docker se deben seguir los siguientes comandos.

### Para generar el jar:
`mvn clean package -DskipTests`

NOTA: es importante saltar los tests puesto que no hay una base de datos aún, esta se genera en el docker compose, pero sin el .jar que se genera con mvn package docker compose fallaría, es decir, de esta forma se evita un error de dependencia circular

### Para levantar los contenedores de mySQL y la app de Spring Boot
`docker-compose up -d --build`

### Para ver el estado de los contenedores
`docker-compose ps`

### Para levantar la APP
`call docker-compose up -d`

## Lanzar todo
Para evitar tener que ejecutar varios comandos para lanzar cada contenedor, tengo un script en bash que crea todo el proyecto y lo levanta:

`.\build.bat`

### Conclusión
