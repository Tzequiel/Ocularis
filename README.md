# Ocularis
**App móvil para consulta y trazabilidad de exámenes oftalmológicos**

Ocularis es una aplicación móvil orientada a centralizar, organizar y dar trazabilidad al acceso y consulta de exámenes oftalmológicos en un contexto académico con datos simulados.

## Identidad Visual
* **Logotipo:** 
<div align="center">
  <img src="docs/diseno/logo.png" alt="Logo Ocularis" width="250">
</div>
* **Paleta de Colores:** 

| Color | Código | Vista Previa |
| :--- | :--- | :--- |
| **Fondo** | `#FFFFFA` | <img src="https://placehold.co/50x50/FFFFFA/FFFFFA.png" width="20" height="20"> |
| **Texto** | `#0D5C63` | <img src="https://placehold.co/50x50/0D5C63/0D5C63.png" width="20" height="20"> |
| **Secundario** | `#44A1A0` | <img src="https://placehold.co/50x50/44A1A0/44A1A0.png" width="20" height="20"> |
| **Adicional** | `#78CDD7` | <img src="https://placehold.co/50x50/78CDD7/78CDD7.png" width="20" height="20"> |
| **Principal** | `#247B7B` | <img src="https://placehold.co/50x50/247B7B/247B7B.png" width="20" height="20"> |

## Flujo de Usuario
```mermaid
stateDiagram-v2
    direction TB
    [*] --> Login: Iniciar App
    
    Login --> ValidarCredenciales: Ingresar datos
    ValidarCredenciales --> Login: Credenciales incorrectas
    ValidarCredenciales --> Inicio: Credenciales correctas
    
    Inicio --> DecisionAccion: Seleccionar menú
    
    DecisionAccion --> FormularioRegistro: Nuevo Examen (Tecnólogo)
    DecisionAccion --> Historial: Consultar (Médico/Paciente)
    DecisionAccion --> [*]: Cerrar Sesión
    
    FormularioRegistro --> AdjuntarArchivo: Completar datos
    AdjuntarArchivo --> ValidarDatos: Subir documento
    
    ValidarDatos --> FormularioRegistro: Faltan campos obligatorios
    ValidarDatos --> DetalleExamen: Datos válidos (Guardar)
    
    Historial --> Filtrar: Aplicar filtros
    Filtrar --> Historial: Actualizar lista
    Historial --> DetalleExamen: Seleccionar examen
    
    DetalleExamen --> Inicio: Volver al menú
```

## Pantallas Principales
Los diseños de las interfaces de Ocularis se encuentran ubicados en el repositorio dentro del directorio `docs/diseno/interfaces/`. Las pantallas generadas para este MVP son:
* Login
* Inicio / Búsqueda
* Registro de Atención
* Historial de Exámenes
* Registro de Examen (Subir Archivos)
* Resumen / Detalle

## Tecnologías Utilizadas
* **Google Stitch:** Generación y prototipado de UI con IA.
* **coolors.co:** Definición de la paleta de colores.
* **planttext.com:** Generación del diagrama de flujo UML.
* **Material Design 3 (m3.material.io):** Sistema de diseño y componentes base.

## Integrantes
**Equipo Bizcochitos**
* **Richard Hernández:** Líder
* **Cesar Molina:** Frontend
* **Matias Carrasco:** Backend
