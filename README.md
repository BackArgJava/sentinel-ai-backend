# 🛡️ Sentinel AI Mainframe

> **The Advanced AI Terminal.** > A secure, fully containerized intelligent terminal interface that uses Google Gemini AI to execute commands, analyze data, and provide real-time operational support.

![Project Status](https://img.shields.io/badge/status-active-success)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen)
![Security](https://img.shields.io/badge/Spring%20Security-JWT-red)
![Docker](https://img.shields.io/badge/Docker-Containerized-2496ED)
![React](https://img.shields.io/badge/React-18-blue)
![AI Model](https://img.shields.io/badge/AI-Gemini%201.5%20Flash-8E75B2)

---

## 📖 About The Project

**Sentinel AI** is a full-stack, secure application designed to simulate an advanced mainframe terminal. It provides a heavily guarded interface where authenticated "Agents" can communicate directly with a highly tuned AI model.

The system is fully Dockerized for seamless deployment. It features a robust Spring Security layer using JWT (JSON Web Tokens) to ensure that only authorized personnel can access the mainframe, intercepting unauthorized access attempts at both the frontend and backend levels.

### ✨ Key Features
* **🤖 AI-Powered Terminal:** Connects to Google Gemini 1.5 Flash to process complex queries and return terminal-themed responses.
* **🔐 Zero-Trust Security:** Implements Spring Security and JWT authentication. Unauthorized users are immediately blocked and redirected.
* **🐳 Fully Dockerized:** The entire infrastructure (MySQL Database + Spring Boot Backend) runs in isolated Docker containers, ensuring it works perfectly on any machine.
* **⚡ React Frontend Guard:** Dynamic routing protects the dashboard, preventing unauthorized URL access.
* **💾 Persistent Storage:** MySQL database integration for reliable data and user management.

---

## 🛠️ Tech Stack

### Backend (The Core)
* **Language:** Java 21
* **Framework:** Spring Boot 3.2.5
* **Security:** Spring Security & JWT (JSON Web Tokens)
* **AI Integration:** Spring AI 1.1.0 (Google Gemini)
* **Database:** MySQL 8.0+ (Containerized)
* **Build Tool:** Maven

### Frontend (The Interface)
* **Framework:** React.js
* **Tooling:** Vite
* **HTTP Client:** Axios (with Auth Interceptors)
* **Styling:** Custom CSS / Terminal Theme

### DevOps (The Infrastructure)
* **Containerization:** Docker & Docker Compose

---

## ⚙️ System Architecture

```mermaid
graph LR
    A[Agent / React Terminal] -- "JWT Auth + Command" --> B{Spring Security Bouncer}
    B -- "Invalid Token" --> X[Access Denied 403]
    B -- "Valid Token" --> C[Spring Boot Backend]
    C -- "Save/Fetch Agent Data" --> D[(MySQL Database in Docker)]
    C -- "Secure Prompt" --> E{Google Gemini AI}
    E -- "AI Response" --> C
    C -- "Terminal Output" --> A