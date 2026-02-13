# 🛡️ Sentinel AI

> **The Bug Tracker that Thinks.** > An intelligent issue tracking dashboard that uses Google Gemini AI to automatically analyze bugs and suggest solutions in real-time.

![Project Status](https://img.shields.io/badge/status-active-success)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen)
![React](https://img.shields.io/badge/React-18-blue)
![AI Model](https://img.shields.io/badge/AI-Gemini%201.5%20Flash-8E75B2)

---

## 📖 About The Project

**Sentinel AI** is a full-stack application designed to modernize the way developers handle bug reports. Instead of just storing error logs, Sentinel actively "reads" them.

When a user submits a bug report (e.g., a stack trace or error description), the backend intercepts the data, consults **Google Gemini AI**, and appends a likely solution or explanation to the ticket before saving it to the database.

### ✨ Key Features
* **🤖 AI-Powered Analysis:** Automatically suggests fixes for bugs using Google Gemini 1.5 Flash.
* **⚡ Real-Time Dashboard:** Built with React & Vite for instant updates.
* **🔐 Secure Backend:** Robust Spring Boot architecture with Spring Data JPA.
* **💾 Persistent Storage:** MySQL database integration for reliable data keeping.
* **RESTful API:** Clean communication between Frontend and Backend.

---

## 🛠️ Tech Stack

### Backend (The Brain)
* **Language:** Java 21
* **Framework:** Spring Boot 3.2.5
* **AI Integration:** Spring AI 1.1.0 (Google Gemini)
* **Database:** MySQL 8.0+
* **Build Tool:** Maven

### Frontend (The Face)
* **Framework:** React.js
* **Tooling:** Vite
* **HTTP Client:** Axios
* **Styling:** CSS Modules / Standard CSS

---

## ⚙️ Architecture

```mermaid
graph LR
    A[User / React Frontend] -- JSON --> B[Spring Boot Backend]
    B -- Save Data --> C[(MySQL Database)]
    B -- "How do I fix this?" --> D{Google Gemini AI}
    D -- "Here is the solution..." --> B
    B -- Updated Issue --> A
