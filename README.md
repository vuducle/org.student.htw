# OnceVerse 🎀💖
*A dedicated social platform for TWICE fans (ONCEs) built with Spring Boot*

## Table of Contents
- [Concept](#-concept)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Installation](#-installation)
- [API Endpoints](#-api-endpoints)
- [Database Schema](#-database-schema)
- [Roadmap](#-roadmap)
- [Contributing](#-contributing)
- [License](#-license)

## 🌟 Concept
OnceVerse is a specialized social media platform where ONCEs can:
- Share TWICE-related content
- Connect with other fans
- Get updates about TWICE activities
- Participate in fan events

## ✨ Features
### Current Features
- 👤 User authentication (HttpSession)
- 📝 TWICE-themed posts with:
    - Member tagging (#Nayeon #Sana etc.)
    - Album/era categorization
- 💬 Comment system
- ❤️ Like/reaction system 
- 🔍 Search other ONCEs

### Upcoming Features
- 🎧 Real-time chat (WebSocket)
- 🎫 Virtual fan meeting system
- 📅 Comeback countdowns

## 🛠️ Tech Stack
**Backend:**
- Java 17
- Spring Boot 3.1
- Spring Security
- Spring Data JPA
- WebSocket (Future implementation)


**Database:**
- PostgreSQL (Primary)


## 📥 Installation
### Prerequisites
- Java 17+
- PostgreSQL 14+
- Gradle

### Setup
1. Clone repository:
   ```bash
   git clone https://github.com/vuducle/org.student.htw.git
   cd org.student.htw
   
### API Endpoints

#### TwiceController (Authentification)
| Method | Endpoint                          | Description                   |
|--------|-----------------------------------|-------------------------------|
| POST   | `/api/v1/twice`                   | Create an ONCE                |
| PUT    | `/api/v1/twice/editOnce`          | Edit ONCEs profile            |
| POST   | `/api/v1/twice/login`             | Login with credentials        |
| POST   | `/api/v1/twice/logout`            | Logout an ONCE                |
| GET    | `/api/v1/twice/user/me`           | Get current ONCEs credentials |
| GET    | `/api/v1/twice/{username}`        | Get ONCE by *username*        |
| GET    | `/api/v1/twice?search=${query}`   | Get ONCE by the query param   |

#### TwicePostController (Post, Comment and Upvote)
| Method | Endpoint                                         | Description                                |
|--------|--------------------------------------------------|--------------------------------------------|
| POST   | `/api/v1/twice-post`                             | Create a Twice post                        |
| GET    | `/api/v1/twice-post`                             | Get all Twice posts                        |
| GET    | `/api/v1/twice-post/post/{onceId}`               | Get a Twice post by Id                     |
| DELETE | `/api/v1/twice-post/post/{onceId}`               | Delete a Twice post by Id with credentials |
| POST   | `/api/v1/twice-post/post/{onceId}/upvote`        | Upvote a post by Id                        |
| POST   | `/api/v1/twice-post/post/{onceId}/comment`       | Comment a post by Id                       |
| POST   | `/api/v1/twice-post/comment/{commmentId}/upvote` | Upvote a comment by Id                     |



### Roadmap

#### Phase 1 (Current)
- Core posting functionality
- Basic authentication
- Search functionality

#### Phase 2 (In Development)
- WebSocket chat
- Member tagging system
- Album/era timeline

#### Phase 3 (Planned)
- Notification system
- Concert viewing parties
- Fan project coordination
