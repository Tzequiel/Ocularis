# Ocularis
**App móvil para consulta y trazabilidad de exámenes oftalmológicos**

Ocularis es una aplicación móvil orientada a centralizar, organizar y dar trazabilidad al acceso y consulta de exámenes oftalmológicos en un contexto académico con datos simulados.

## Identidad Visual
* **Logotipo:** Ubicado en `docs/diseno/logo.png`
* **Paleta de Colores:** 
  * Principal: #247B7B
  * Secundario: #44A1A0
  * Fondo: #FFFFFA
  * Texto: #0D5C63
  * Adicional: #78CDD7

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
