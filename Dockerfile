# =========================================================================
# ETAPA 1: Construcción (Build Stage)
# Esta etapa compila tu aplicación Spring Boot y genera el JAR ejecutable.
# =========================================================================
FROM maven:3-openjdk-17 AS build
# Establece el directorio de trabajo dentro del contenedor para la construcción
WORKDIR /app

# Copia los archivos de configuración de Maven para aprovechar el cacheo de dependencias
# Esto asegura que si solo cambian los archivos de código, las dependencias no se descarguen de nuevo
COPY pom.xml .
COPY settings.xml .

# Descarga las dependencias de Maven (solo si pom.xml o settings.xml cambian)
# Esto acelera las reconstrucciones si no hay cambios en las dependencias
RUN mvn dependency:go-offline

# Copia todo el código fuente de tu proyecto
COPY src ./src

# Compila el proyecto y empaqueta la aplicación en un JAR
# -DskipTests para saltar las pruebas durante el build de Docker (puedes quitarlas si quieres que se ejecuten)
RUN mvn clean package -DskipTests

# =========================================================================
# ETAPA 2: Aplicación (Application Stage)
# Esta etapa toma el JAR compilado de la etapa anterior y lo prepara para ejecución.
# =========================================================================
# Usa una imagen base ligera de OpenJDK solo con el JRE para la ejecución
FROM openjdk:25-ea-17-windowsservercore-1809

# Establece el directorio de trabajo dentro del contenedor para la aplicación
WORKDIR /app

# Copia el JAR ejecutable de la etapa de construcción a la imagen final
# Asegúrate de que el patrón *.jar coincida con el nombre de tu JAR, por ejemplo: ProjectBackend_BACK-0.0.1-SNAPSHOT.jar
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto por defecto de Spring Boot (8080)
# Render usará este puerto para enrutar el tráfico externo a tu aplicación
EXPOSE 8080

# Define el comando para ejecutar tu aplicación cuando el contenedor se inicie
# Puedes añadir aquí opciones de JVM si las necesitas (ej: -Xmx512m)
ENTRYPOINT ["java", "-jar", "app.jar"]

# =========================================================================
# Consideraciones adicionales para Render:
# - Render detectará este Dockerfile automáticamente.
# - Las variables de entorno para la base de datos, credenciales, etc.,
#   se configurarán directamente en el dashboard de Render, no en el Dockerfile.
# - El puerto 8080 expuesto aquí es el puerto interno del contenedor. Render
#   asignará un puerto público y lo mapeará a este.
# =========================================================================