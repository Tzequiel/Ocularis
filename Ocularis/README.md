# Ocularis
**App móvil para consulta y trazabilidad de exámenes oftalmológicos**

Ocularis es una aplicación móvil desarrollada en Android (Jetpack Compose) orientada a centralizar, organizar y dar trazabilidad al acceso, gestión y consulta de exámenes e historiales clínicos oftalmológicos para especialistas y profesionales de la salud.

---

## 🎨 Identidad Visual
* **Logotipo:** 
<div align="center">
  <img src="docs/diseno/logo.png" alt="Logo Ocularis" width="250">
</div>

* **Paleta de Colores Oficial:** 

| Color | Código | Vista Previa |
| :--- | :--- | :--- |
| **Fondo** | `#FFFFFA` | <img src="https://placehold.co/50x50/FFFFFA/FFFFFA.png" width="20" height="20"> |
| **Texto** | `#0D5C63` | <img src="https://placehold.co/50x50/0D5C63/0D5C63.png" width="20" height="20"> |
| **Secundario** | `#44A1A0` | <img src="https://placehold.co/50x50/44A1A0/44A1A0.png" width="20" height="20"> |
| **Adicional** | `#78CDD7` | <img src="https://placehold.co/50x50/78CDD7/78CDD7.png" width="20" height="20"> |
| **Principal** | `#247B7B` | <img src="https://placehold.co/50x50/247B7B/247B7B.png" width="20" height="20"> |

---

## 🏛️ Arquitectura y Base de Datos (3NF)
El proyecto implementa el patrón **MVVM (Model-View-ViewModel)** junto con **Room Database (SQLite)** normalizada en **Tercera Forma Normal (3NF)** para garantizar la persistencia local permanente:
* **Entidades (`Entities`):** `UsuarioEntity`, `PacienteEntity`, `EspecialistaEntity`, `AlertaClinicaEntity`, `ConsultaMedicaEntity`, `ExamenEntity`.
* **Acceso a Datos (`DAOs`):** Consultas reactivas basadas en **Kotlin Coroutines** y `Flow`.
* **Repositorio (`Repository`):** Capa única de verdad que alimenta al `AppViewModel`.
* **Poblador Inicial (`Seeder`):** Carga automática de registros clínicos de prueba al iniciar la aplicación.
* **Persistencia Offline y Sincronización:** Conexión con InsForge Cloud API y Room, indicador sutil de estado de sincronización y DataStore para preferencias de accesibilidad.

---

## ♿ Accesibilidad Visual Obligatoria (WCAG 2.2 AA)
Ocularis está especialmente adaptada a personas con discapacidades o dificultades visuales, permitiendo consultar exámenes y navegar de manera autónoma:
* **Alto Contraste Clínico:** Modo de contraste mejorado para lecturas diagnósticas persistido con Jetpack DataStore.
* **Escala Dinámica de Texto:** Ajuste de tamaño de fuente (Normal 1.0x, Grande 1.25x, Extra Grande 1.5x).
* **Lector de Voz Integrado (TextToSpeech):** Botón de lectura en voz alta para informes y resultados de exámenes médicos.

---

## 📱 Pantallas Principales implementadas (MVP)
Todas las interfaces están construidas con **Jetpack Compose** y **Material 3**:
1. **Login (`LoginScreen.kt`):** Acceso seguro con validación dinámica acumulativa de contraseña (mínimo 8 caracteres, mayúsculas, minúsculas, números y caracteres especiales).
2. **Inicio / Pacientes (`HomeScreen.kt`):** Listado dinámico de pacientes recientes desde Room, barra de búsqueda, indicador de sincronización y filtros rápidos.
3. **Nueva Atención (`AtencionScreen.kt`):** Formulario clínico con buscador autocomplete de pacientes de la base de datos, selector de fecha nativo y chips rápidos.
4. **Historial de Atenciones / Directorio (`HistorialScreen.kt`):** Consulta global de pacientes, exploraciones y exámenes sincronizados.
5. **Detalle del Paciente (`DetallePacienteScreen.kt`):** Expediente completo, datos de contacto, alertas oftalmológicas (miopía, glaucoma), próximo control e historial de consultas.
6. **Lista de Exámenes (`ListaExamenes.kt`):** Pantalla intermedia para la consulta y filtrado de exámenes sincronizados desde PACS.
7. **Detalle del Examen (`DetalleExamen.kt`):** Generación automática de PDF mediante `PdfDocument` y visualizador interactivo nativo con **`PdfRenderer`** (soporte de zoom y desplazamiento).
8. **Perfil del Especialista (`PerfilScreen.kt`):** Panel del médico con estado de disponibilidad interactivo, estadísticas mensuales, credenciales y preferencias de accesibilidad.

---

## ⚡ Recursos Nativos de Android e Integraciones
* **Biometría Nativa (`BiometricPrompt`):** Autenticación por huella dactilar o reconocimiento facial en el Login.
* **Telefonía y Mensajería (`Intent.ACTION_DIAL` / `smsto:`):** Llamadas y envío de SMS directos a los pacientes desde su perfil.
* **Compartir del Sistema (`Android ShareSheet`):** Envío de informes clínicos a otras aplicaciones mediante `Intent.ACTION_SEND`.
* **Impresión y Visor PDF Nativo:** Generación con `PdfDocument` y renderizado de páginas con `PdfRenderer`.
* **Síntesis de Voz (`TextToSpeech`):** Lectura en voz alta en español del informe médico completo ("Escuchar Informe Completo 🔊").
* **Fecha y Hora (`DatePickerDialog`):** Selector de calendario nativo para agendamientos.

---

## 📊 Flujo de Usuario
```mermaid
stateDiagram-v2
    direction TB
    [*] --> Login: Iniciar App
    
    Login --> ValidarCredenciales: Ingresar datos / Biometría
    ValidarCredenciales --> Login: Credenciales incorrectas
    ValidarCredenciales --> Inicio: Acceso autorizado
    
    Inicio --> DetallePaciente: Seleccionar paciente
    DetallePaciente --> ListaExamenes: Ver lista completa de exámenes
    ListaExamenes --> DetalleExamen: Seleccionar examen específico
    
    Inicio --> NuevaAtencion: Registrar Nueva Atención
    NuevaAtencion --> Inicio: Guardar en Base de Datos Room
    
    Inicio --> Historial: Pestaña Directorio de Pacientes
    Inicio --> Perfil: Pestaña Perfil del Especialista
    
    Perfil --> Login: Cerrar Sesión Segura
    Inicio --> [*]: Salir
```

## Pantallas Principales
Los diseños de las interfaces de Ocularis se encuentran ubicados en el repositorio dentro del directorio `docs/diseno/interfaces/`. Las pantallas generadas para este MVP son:
* Login
* Inicio / Búsqueda
* Registro de Atención
* Historial de Exámenes
* Directorio de Pacientes
* Resumen / Detalle y Visor PDF

---

## 🧪 Ejecución de Pruebas y Calidad
El proyecto cuenta con pruebas unitarias e instrumentadas que aseguran la lógica de negocio y accesibilidad:
```bash
# Ejecutar pruebas unitarias (JUnit 4 + MockK)
./gradlew app:testDebugUnitTest
```

---

## 📦 Generación de APK Release Firmada
Para construir el binario firmado para distribución en modo release:
```bash
./gradlew app:assembleRelease
```
El archivo APK generado se ubicará en: `app/build/outputs/apk/release/app-release.apk`

---

## Tecnologías Utilizadas
* **Kotlin & Jetpack Compose & Material 3:** Desarrollo nativo moderno.
* **MVVM + Room + DataStore:** Arquitectura robusta y persistencia offline.
* **Retrofit & InsForge:** Comunicación con API REST y servicios cloud.
* **PdfDocument & PdfRenderer:** Generación y visualización avanzada de documentos médicos.
* **TextToSpeech & TalkBack:** Accesibilidad universal WCAG 2.2 AA.
* **Google Stitch / coolors.co / planttext.com:** Prototipado, paleta de colores y diagramas UML.

---

## Integrantes
**Equipo Bizcochitos**
* **Richard Hernández:** Líder
* **Cesar Molina:** Frontend
