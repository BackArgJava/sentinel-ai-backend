# 🛡️ Sentinel AI

> **El Rastreador de Bugs que Piensa.**
> Un dashboard inteligente de gestión de incidencias que utiliza la IA de Google Gemini para analizar errores y sugerir soluciones automáticamente en tiempo real.

> *[🇺🇸 Read in English](README.md)*

![Estado del Proyecto](https://img.shields.io/badge/status-activo-success)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen)
![React](https://img.shields.io/badge/React-18-blue)
![Modelo IA](https://img.shields.io/badge/AI-Gemini%201.5%20Flash-8E75B2)

---

## 📖 Sobre el Proyecto

**Sentinel AI** es una aplicación Full-Stack diseñada para modernizar la forma en que los desarrolladores manejan los reportes de errores. En lugar de simplemente almacenar registros de errores (logs), Sentinel los "lee" activamente.

Cuando un usuario envía un reporte de error (por ejemplo, un *stack trace* o una descripción del fallo), el backend intercepta los datos, consulta a **Google Gemini AI**, y adjunta una solución probable o una explicación técnica al ticket antes de guardarlo en la base de datos.

### ✨ Características Principales
* **🤖 Análisis Potenciado por IA:** Sugiere soluciones automáticamente para los bugs utilizando Google Gemini 1.5 Flash.
* **⚡ Dashboard en Tiempo Real:** Construido con React & Vite para actualizaciones instantáneas.
* **🔐 Backend Seguro:** Arquitectura robusta en Spring Boot con Spring Data JPA.
* **💾 Almacenamiento Persistente:** Integración con base de datos MySQL para un resguardo de datos confiable.
* **API RESTful:** Comunicación limpia y eficiente entre Frontend y Backend.

---

## 🛠️ Stack Tecnológico

### Backend (El Cerebro)
* **Lenguaje:** Java 21
* **Framework:** Spring Boot 3.2.5
* **Integración IA:** Spring AI 1.1.0 (Google Gemini)
* **Base de Datos:** MySQL 8.0+
* **Herramienta de Construcción:** Maven

### Frontend (La Cara)
* **Framework:** React.js
* **Herramientas:** Vite
* **Cliente HTTP:** Axios
* **Estilos:** CSS Modules / Standard CSS

---

## ⚙️ Arquitectura

```mermaid
graph LR
    A[Usuario / Frontend React] -- JSON --> B[Backend Spring Boot]
    B -- Guardar Datos --> C[(Base de Datos MySQL)]
    B -- "¿Cómo arreglo esto?" --> D{IA Google Gemini}
    D -- "Aquí está la solución..." --> B
    B -- Incidencia Actualizada --> A
