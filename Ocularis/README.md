# 👁️ Ocularis - Sistema Oftalmológico Móvil

**Ocularis** es una aplicación móvil nativa desarrollada en **Kotlin** con **Jetpack Compose**, **Material 3** y arquitectura **MVVM**, orientada a la gestión clínica oftalmológica en tiempo real para centros de salud, especialidades médicas y pacientes.

---

## 🌟 Características Principales

1. **Gestión Clínica Completa (MVVM & Room):**
   - Autenticación multirrol: Administrador, Especialista (Médico Oftalmólogo) y Paciente.
   - Directorio de pacientes en tiempo real con alertas clínicas (Glaucoma, Miopía, Retinopatía, Cataratas).
   - Registro de nuevas atenciones clínicas, recetas oftalmológicas e historial médico.
   - Visualización detallada de exámenes de diagnóstico (OCT Macular, Campimetría, Tonometría, Fondo de Ojo) con apoyo multimedia e informes narrados.

2. **Accesibilidad Visual Integrada (WCAG 2.2 AA & Jetpack DataStore):**
   - **Persistencia con DataStore:** Guardado reactivo de preferencias accesibles (`AccessibilityPreferencesManager`).
   - **Alto Contraste Clínico:** Tema de alto contraste optimizado para lectura rápida y angiografías.
   - **Escala Dinámica de Texto:** Ajuste de tamaño de fuente a nivel global (Normal 1.0x, Grande 1.25x, Extra Grande 1.5x).
   - **Lector de Voz (TextToSpeech):** Lectura en voz alta automatizada para informes de exámenes.

3. **Backend Cloud InsForge & Persistencia Offline:**
   - Integración con API REST de **InsForge Cloud Auth & Microservices**.
   - Respaldo reactivo en base de datos local con Room (`OcularisDatabase`).
   - **Indicador sutil de Sincronización:** Chip visual de estado en tiempo real (Sincronizado, Sincronizando..., Modo Offline).

4. **Suite de Pruebas Unitarias e Instrumentadas:**
   - Pruebas unitarias con **JUnit 4**, **MockK** y `kotlinx-coroutines-test` para la lógica de negocio de `AppViewModel`.
   - Pruebas de IU con **Compose UI Test** (`createComposeRule`) para validación de renderizado e interacción.

---

## 🛠️ Tecnologías y Librerías

- **Lenguaje:** Kotlin 2.0+
- **UI:** Jetpack Compose, Material 3, Material Icons Extended
- **Arquitectura:** MVVM (Model-View-ViewModel) + Clean Data Layer
- **Persistencia Local:** Room Database + Jetpack DataStore Preferences
- **Red:** Retrofit 2 + Gson + OkHttp3 Logging Interceptor
- **Pruebas:** JUnit 4, MockK, Coroutines Test, Compose UI Test JUnit4
- **Seguridad / Biometría:** AndroidX Biometric

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
- **Gestión de Pacientes:** API Rest endpoints sync con Room.

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
