# Walkthrough - Implementación de Base de Datos Local con Room en Ocularis

Se ha implementado exitosamente la persistencia local con Room Database en la aplicación Android **Ocularis**, estructurada sobre la rama Git `feature/BaseDeDatos`.

## Resumen de Cambios

### 1. Rama Git y Configuración Gradle
- **Rama**: Creada y posicionada en `feature/BaseDeDatos`.
- **Dependencias**: Se agregaron Room (`room-runtime`, `room-ktx`, `room-compiler`) y el plugin KSP (`2.0.21-1.0.25`) en `gradle/libs.versions.toml` y `app/build.gradle.kts`.

### 2. Capa de Datos (Entidades, DAOs y Base de Datos)
- **Entidades**:
  - `UsuarioEntity`: Manejo de credenciales, roles (`ESPECIALISTA` o `PACIENTE`) y referencias.
  - `ConsultaMedicaEntity`: Registro de atenciones médicas, diagnósticos y recetas.
  - `ExamenEntity`: Exploración tecnológica, estado, fechas y tipo de examen.
  - `PacienteEntity`: Expediente clínico, previsiones, RUT, ficha y alertas clínicas.
  - `EspecialistaEntity`: Perfil del médico, especialidad y disponibilidad.
- **DAOs**: `UsuarioDao`, `ConsultaMedicaDao`, `ExamenDao`, `PacienteDao`, `EspecialistaDao` con operaciones de inserción (`OnConflictStrategy.REPLACE`) y consultas reactivas con `Flow`.
- **Base de Datos y Seeding**: `OcularisDatabase.kt` configurado como Singleton con `RoomDatabase.Callback` para sembrar datos iniciales de prueba (Valentina Morales, Carlos Mendoza, Matías Alarcón, Dr. Roberto Silva, etc.).
- **Repositorio**: `OcularisRepository.kt` encapsulando el acceso a los DAOs.

### 3. Arquitectura y UI
- **`AppViewModel`**: Actualizado a `AndroidViewModel` para conectar de forma reactiva con `OcularisRepository` mediante `StateFlow` (`pacientes`, `examenes`, `consultas`) y funciones de persistencia.
- **`HomeScreen`**: Consumo dinámico y reactivo de la lista de pacientes desde la base de datos Room.
- **`AtencionScreen`**: Registro de nuevas atenciones y exámenes médicos persistidos físicamente en almacenamiento local.

## Verificación de Compilación
- Se ejecutó `app:assembleDebug` obteniendo un **Build Successful** sin errores de compilación.
