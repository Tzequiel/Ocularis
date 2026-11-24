# 👁️ Ocularis - Sistema Oftalmológico Móvil

**Ocularis** es una aplicación móvil nativa desarrollada en **Kotlin** con **Jetpack Compose**, **Material 3** y arquitectura **MVVM**, orientada a la gestión clínica oftalmológica avanzada en tiempo real para centros de salud, especialistas médicos y pacientes, cumpliendo rigurosamente con los estándares de accesibilidad visual y normativas clínicas.

---

## 🌟 Características Principales y Arquitectura

1. **Gestión Clínica Completa (MVVM & Room / InsForge API):**
   - Autenticación multirrol segura: Administrador, Especialista (Médico Oftalmólogo) y Paciente.
   - Directorio de pacientes en tiempo real con alertas clínicas (Glaucoma, Miopía, Retinopatía, Cataratas).
   - Registro de nuevas atenciones clínicas, recetas oftalmológicas e historial médico completo.
   - Visualización detallada de exámenes de diagnóstico (OCT Macular, Campimetría, Tonometría, Fondo de Ojo) con apoyo multimedia e informes narrados.

2. **Accesibilidad Visual Avanzada (WCAG 2.2 AA & Jetpack DataStore):**
   - **Persistencia con DataStore:** Guardado reactivo de preferencias accesibles (`AccessibilityPreferencesManager`).
   - **Alto Contraste Clínico:** Tema de alto contraste optimizado para lectura rápida.
   - **Escala Dinámica de Texto:** Ajuste de tamaño de fuente a nivel global (Normal 1.0x, Grande 1.25x, Extra Grande 1.5x).
   - **Lector de Voz (TextToSpeech):** Lectura en voz alta automatizada para informes de exámenes médicos.

3. **Backend Cloud InsForge & Persistencia Offline:**
   - Integración con API REST de **InsForge Cloud Auth & Microservices**.
   - Respaldo reactivo en base de datos local con Room (`OcularisDatabase`).
   - **Indicador sutil de Sincronización:** Chip visual de estado en tiempo real (Sincronizado, Sincronizando..., Modo Offline).

4. **Suite de Pruebas Unitarias e Instrumentadas:**
   - Pruebas unitarias con **JUnit 4**, **MockK** y `kotlinx-coroutines-test` para la lógica de negocio de `AppViewModel`.
   - Pruebas de IU con **Compose UI Test** (`createComposeRule`) para validación de renderizado e interacción.

---

## 👥 Flujos de Usuarios (User Flows)

El sistema opera bajo tres perfiles diferenciados según el rol autenticado:

### 1. 🛡️ Administrador del Sistema
- **Gestión de Cuentas:** Permite dar de alta nuevos usuarios (Especialistas y Pacientes) integrados con el servidor de autenticación y base de datos.
- **Panel de Control (Dashboard):** Visualización de métricas operativas del centro oftalmológico (total de atenciones, tasa de exámenes diarios, estado de interoperabilidad HL7/DICOM y sincronización de base de datos).
- **Control de Accesos:** Auditoría y eliminación de registros de usuarios en el sistema.

### 2. 🩺 Especialista / Médico Oftalmólogo
- **Agenda Diaria:** Visualización de pacientes agendados, filtrado por estado de atención (*Agenda Pendiente* vs *Al Día*) y acceso directo a fichas médicas.
- **Nueva Atención (Ingreso Clínico):** Formulario validado con selector de pacientes desde la base de datos (buscador autocomplete), selector de fecha (calendario nativo), sucursal, médico a cargo y motivo de consulta con chips rápidos.
- **Detalle de Examen y Visor PDF:** 
  - Generación automática de informes en PDF mediante `PdfDocument` con los datos reales del paciente y hallazgos clínicos.
  - Visualización nativa de las páginas del documento dentro de la app utilizando **`PdfRenderer`** (renderizado en bitmaps).
  - Opción de descarga local al almacenamiento del dispositivo y lectura por voz (*TextToSpeech*) del expediente.

### 3. 👤 Paciente
- **Portal Personal:** Bienvenida personalizada con resumen de su estado de salud visual y previsión (ej. Fonasa B, Isapre Banmédica).
- **Mis Exámenes:** Consulta de su historial de estudios oftalmológicos (OCT, Tonometría, Campimetría) con detalle de ojo evaluado (OD/OI) y estado de validación médica.
- **Configuración de Accesibilidad:** Personalización en tiempo real del tamaño de fuente y activación del alto contraste clínico persistido mediante DataStore.

---

## 🛠️ Tecnologías y Librerías

- **Lenguaje:** Kotlin 2.0+
- **UI:** Jetpack Compose, Material 3, Material Icons Extended
- **Arquitectura:** MVVM (Model-View-ViewModel) + Clean Data Layer
- **Persistencia Local:** Room Database + Jetpack DataStore Preferences
- **Red:** Retrofit 2 + Gson + OkHttp3 Logging Interceptor
- **Generación y Visor PDF:** `android.graphics.pdf.PdfDocument` + `android.graphics.pdf.PdfRenderer`
- **Accesibilidad / Voz:** `TextToSpeech` API
- **Pruebas:** JUnit 4, MockK, Coroutines Test, Compose UI Test JUnit4

---

## 📂 Estructura del Proyecto

```
app/src/
├── main/java/com/duoc/ocularis/
│   ├── data/
│   │   ├── AccessibilityPreferencesManager.kt   # Gestor DataStore de Accesibilidad
│   │   ├── OcularisDatabase.kt                  # Base de datos Room
│   │   ├── OcularisRepository.kt                # Repositorio de datos unificado
│   │   ├── dao/                                # DAOs de Room
│   │   ├── entity/                             # Entidades Room
│   │   └── remote/                             # Cliente Retrofit & DTOs InsForge API
│   ├── model/                                  # Modelos del dominio y Roles
│   ├── navigation/                             # Configuración de NavHost y Screens
│   ├── ui/
│   │   ├── components/                         # Componentes reutilizables (SyncIndicator, etc.)
│   │   ├── screens/                            # Pantallas (Login, Home, Perfil, Exámenes, etc.)
│   │   └── theme/                              # Colores, Tipografía y OcularisTheme con DataStore
│   └── viewmodel/
│       └── AppViewModel.kt                     # ViewModel principal con StateFlows
└── test/java/com/duoc/ocularis/
    ├── MainDispatcherRule.kt                   # Regla JUnit para Coroutines
    └── viewmodel/AppViewModelTest.kt           # Pruebas unitarias con JUnit & MockK
```

---

## 🌐 Endpoints Backend InsForge

- **Autenticación (Login):** `POST /auth/v1/token?grant_type=password`
- **Registro (Register):** `POST /auth/v1/signup`
- **Gestión de Pacientes y Consultas:** API Rest sincronizada con PostgREST tables.

---

## 🧪 Ejecución de Pruebas

Para ejecutar la suite de pruebas unitarias desde la terminal o Gradle wrapper:

```bash
./gradlew app:testDebugUnitTest
```

---

## 📦 Generación de APK Release Firmada

Para construir el binario firmado para distribución en modo release:

```bash
./gradlew app:assembleRelease
```

El archivo APK generado se ubicará en:
`app/build/outputs/apk/release/app-release.apk`

---

## 👨‍💻 Integrantes del Equipo de Desarrollo

- **Desarrollador Principal / Arquitectura MVVM & UI:** Tzequiel y equipo de desarrollo Ocularis Health Tech.
- **Evaluación Final Transversal (EFT) · Duoc UC**
