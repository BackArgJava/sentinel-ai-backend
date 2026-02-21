# 🛡️ Sentinel AI Mainframe

> **La Terminal de IA Avanzada.**
> Una interfaz de terminal inteligente, segura y completamente en contenedores que utiliza la IA de Google Gemini para ejecutar comandos, analizar datos y proporcionar soporte operativo en tiempo real.

> *[🇺🇸 Read in English](README.md)*

![Estado del Proyecto](https://img.shields.io/badge/status-activo-success)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen)
![Seguridad](https://img.shields.io/badge/Spring%20Security-JWT-red)
![Docker](https://img.shields.io/badge/Docker-En%20Contenedores-2496ED)
![React](https://img.shields.io/badge/React-18-blue)
![Modelo IA](https://img.shields.io/badge/AI-Gemini%201.5%20Flash-8E75B2)

---

## 📖 Sobre el Proyecto

**Sentinel AI** es una aplicación Full-Stack segura diseñada para simular una terminal de mainframe avanzada. Proporciona una interfaz fuertemente protegida donde "Agentes" autenticados pueden comunicarse directamente con un modelo de IA altamente optimizado.

El sistema está completamente en contenedores (Dockerizado) para un despliegue sin problemas. Cuenta con una robusta capa de Spring Security utilizando JWT (JSON Web Tokens) para asegurar que solo el personal autorizado pueda acceder al mainframe, interceptando intentos de acceso no autorizados tanto a nivel de frontend como de backend.

### ✨ Características Principales
* **🤖 Terminal Potenciada por IA:** Se conecta a Google Gemini 1.5 Flash para procesar consultas complejas y devolver respuestas con temática de terminal.
* **🔐 Seguridad Zero-Trust:** Implementa autenticación con Spring Security y JWT. Los usuarios no autorizados son bloqueados y redirigidos inmediatamente.
* **🐳 Completamente en Contenedores (Docker):** Toda la infraestructura (Base de Datos MySQL + Backend Spring Boot) se ejecuta en contenedores Docker aislados, asegurando que funcione perfectamente en cualquier máquina.
* **⚡ Guardia de Frontend en React:** El enrutamiento dinámico protege el dashboard, evitando el acceso a URLs no autorizadas.
* **💾 Almacenamiento Persistente:** Integración con base de datos MySQL para un manejo confiable de datos y usuarios.

---

## 🛠️ Stack Tecnológico

### Backend (El Núcleo)
* **Lenguaje:** Java 21
* **Framework:** Spring Boot 3.2.5
* **Seguridad:** Spring Security & JWT (JSON Web Tokens)
* **Integración IA:** Spring AI 1.1.0 (Google Gemini)
* **Base de Datos:** MySQL 8.0+ (En Contenedores)
* **Herramienta de Construcción:** Maven

### Frontend (La Interfaz)
* **Framework:** React.js
* **Herramientas:** Vite
* **Cliente HTTP:** Axios (con Interceptores de Autenticación)
* **Estilos:** Custom CSS / Tema de Terminal

### DevOps (La Infraestructura)
* **Contenedorización:** Docker & Docker Compose

---

## ⚙️ Arquitectura del Sistema

```mermaid
graph LR
    A[Agente / Terminal React] -- "Autenticación JWT + Comando" --> B{Guardia Spring Security}
    B -- "Token Inválido" --> X[Acceso Denegado 403]
    B -- "Token Válido" --> C[Backend Spring Boot]
    C -- "Guardar/Obtener Datos" --> D[(Base de Datos MySQL en Docker)]
    C -- "Prompt Seguro" --> E{IA Google Gemini}
    E -- "Respuesta de la IA" --> C
    C -- "Salida de Terminal" --> A