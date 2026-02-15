# 🛡️ Sentinel AI - Panel de Diagnóstico de Errores

## 🇪🇸 Versión en Español

### Acerca del Proyecto
Sentinel AI es una herramienta de diagnóstico Full-Stack diseñada para analizar, explicar y resolver excepciones de backend en Java en tiempo real. Al integrar una API REST de Spring Boot con el último modelo de IA Gemini de Google, actúa como un Desarrollador Senior virtual, proporcionando resoluciones de código instantáneas a través de una interfaz moderna en React.

### 🏗️ Arquitectura y Tecnologías

![Diagrama de Arquitectura](./architecture.png)

Este proyecto demuestra una arquitectura Full-Stack completa y desacoplada:
* **Frontend:** React, Vite, React-Markdown, CSS Personalizado (Tema Oscuro/Cyber)
* **Backend:** Java, Spring Boot, Spring Web, Base de Datos MySQL
* **Integración de IA:** Spring AI, Google Gemini 2.5 Flash
* **Herramientas:** Maven, npm

### 🔒 Enfoque en Seguridad
Un enfoque principal de este proyecto es la ciberseguridad y la gestión segura de credenciales.
* **Variables de Entorno:** Las claves API y credenciales de bases de datos nunca están codificadas en el texto. Se inyectan en tiempo de ejecución utilizando variables de entorno seguras (`${GEMINI_API_KEY}`).
* **Configuración CORS:** El backend está configurado para aceptar de forma segura el intercambio de recursos de origen cruzado desde el puerto específico del frontend (Vite).

### 🚀 Cómo Ejecutar Localmente

**1. Iniciar el Backend (Puerto 8081)**
* Asegúrate de tener instalado Java 17+ y MySQL en ejecución.
* Configura tu variable de entorno: `export GEMINI_API_KEY="tu_api_key_aqui"`
* Ejecuta la aplicación Spring Boot a través de tu IDE o Maven.

**2. Iniciar el Frontend (Puerto 5173)**
```bash
cd sentinel-frontend
npm install
npm run dev