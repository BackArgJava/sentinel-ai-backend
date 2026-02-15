# 🛡️ Sentinel AI - Error Diagnostics Dashboard

[🇪🇸 Leer en Español](#-versión-en-español) | [🇬🇧 Read in English](#-english-version)

---

## 🇬🇧 English Version

### About The Project
Sentinel AI is a Full-Stack diagnostic tool designed to analyze, explain, and solve backend Java exceptions in real-time. By integrating a Spring Boot REST API with Google's latest Gemini AI model, it acts as a virtual Senior Developer, providing instant code resolutions through a modern, responsive React interface.

### 🏗️ Architecture & Tech Stack

![Architecture Diagram](./architecture.png)

This project demonstrates a complete, decoupled Full-Stack architecture:
* **Frontend:** React, Vite, React-Markdown, Custom CSS (Dark/Cyber Theme)
* **Backend:** Java, Spring Boot, Spring Web, MySQL Database
* **AI Integration:** Spring AI, Google Gemini 2.5 Flash
* **Build Tools:** Maven, npm

### 🔒 Security Focus
A major focus of this project is cybersecurity and secure credential management. 
* **Environment Variables:** API keys and sensitive database credentials are never hardcoded. They are injected at runtime using secure environment variables (`${GEMINI_API_KEY}`).
* **CORS Configuration:** The backend is configured to securely accept cross-origin resource sharing from the specific Vite frontend port.

### 🚀 How to Run Locally

**1. Start the Backend (Port 8081)**
* Ensure Java 17+ and MySQL are installed and running.
* Set your environment variable: `export GEMINI_API_KEY="your_api_key_here"`
* Run the Spring Boot application via your IDE or Maven.

**2. Start the Frontend (Port 5173)**
```bash
cd sentinel-frontend
npm install
npm run dev