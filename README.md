# Proyecto de Ingeniería de Software

## Descripción
¡HOLA! Aquí encontrarás lo trabajado en la materia de Ingeniería de Software. Este proyecto consiste en un sistema de servicios de barras que incluye funcionalidades como el registro de técnicos, la gestión de órdenes y la generación de reportes en PDF.

## Análisis

### Día 1: 18/09/2023
========================================================================
Entramos a una llamada por Meet donde vimos el programa y lo analizamos.

**NOTAS:**
1. El programa trata sobre un sistema de servicios de barras.
2. No tiene un inicio de sesión por administrador, por lo que se puede acceder a cualquier parte del programa.
3. No cuenta con una conexión a base de datos, por lo que los datos no se guardan.
4. El botón de cargar la tabla no le vemos una función ya que el usuario pierde tiempo.
5. No es amigable al usuario.
6. Los campos de la tabla se pueden mover.
7. ¿Qué datos vamos a imprimir?

========================================================================

### Día 2: 19/09/2023
========================================================================
En la clase vimos varios diseños para implementar en el nuevo software. Decidimos que el diseño que mejor se adapta a nuestras necesidades es el de la figura 1. (link figma)

Implementamos el nuevo diseño creando un proyecto desde cero y nos permita añadir las nuevas ideas.

**Medidas de los iconos:** 32x32

- Agregaremos en el login, el registro y la tabla.
- Como pendiente nos falta ver qué SGBD utilizar y cómo lanzar el PDF.

========================================================================

### Día 3: 20/09/2023
========================================================================
Creamos la BD, decidimos que fuera en Azure ya que así no tenemos que instalar nada en local y podemos acceder desde cualquier sitio solo con conexión a internet.

Creamos la BD que se llama "IngSoftwareBD" junto con 2 tablas: la tabla de "Registros", "Tecnicos" y la tabla de "Usuarios".

Esto lo hicimos porque así el usuario no puede ingresar al sistema si no cuenta con las credenciales que se encuentran en la tabla de "Usuarios". La tabla "Tecnicos" la usamos para dar de alta a los técnicos que van a dar de alta los registros.

========================================================================

## Estructura del Proyecto

### /CalculoDeBarras/src/main/java/calculodebarras

- **Registro.java:** Clase que maneja los registros de las órdenes.
- **Panel.java:** Interfaz gráfica principal del sistema.
- **Login.java:** Interfaz de inicio de sesión.
- **DatabaseConnection.java:** Clase singleton para manejar la conexión a la base de datos.
- **CalculoDeBarras.java:** Clase principal que inicia la aplicación.

### /calculoBarras/src/calculobarras

- **calculo.java:** Interfaz gráfica alternativa para el manejo de órdenes.

## Contribuciones
Para contribuir a este proyecto, por favor sigue las guías de estilo y asegúrate de que tu código esté bien documentado.

## Licencia
Este proyecto está bajo la Licencia MIT.
