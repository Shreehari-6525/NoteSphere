# 📚 NoteSphere

> A web-based academic resource platform where students can upload, discover, and access notes, question papers, and study materials — organized by subject, year, and unit, with an admin approval workflow to ensure quality.

---

## ✨ Features

- 📤 **File Upload** — Students can upload PDFs and study materials with metadata (title, subject, year, unit)
- ✅ **Admin Approval System** — All uploads go through a `pending → approved / rejected` review flow
- 🔐 **Google Sign-In** — Authentication via Google OAuth for students
- 🗂️ **Organized Content** — Browse notes and question papers filtered by subject, year, and unit
- 🔎 **File Serving** — Approved files are served directly through the app
- 🛠️ **Admin Panel** — Dedicated dashboard to manage and moderate all uploads

---

## 🏗️ Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java Servlets (Jakarta EE 10) |
| Server | Apache Tomcat 11 |
| Database | MySQL 8 |
| Frontend | HTML5, CSS3, JavaScript |
| Auth | Google OAuth 2.0 |
| IDE | Eclipse IDE (Dynamic Web Project) |

---

## 📁 Project Structure

```
NoteSphere/
├── src/
│   └── main/
│       ├── java/
│       │   └── website/
│       │       ├── AdminServlet.java       # Handles admin approve/reject actions
│       │       ├── DBConnection.java       # MySQL connection utility
│       │       ├── FileServlet.java        # Serves uploaded files
│       │       └── UploadServlet.java      # Handles multipart file uploads
│       └── webapp/
│           ├── index.html                  # Landing / login page
│           ├── notes.html                  # Browse notes
│           ├── upload.html                 # Upload form
│           ├── question_papers.html        # Browse question papers
│           ├── profile.html                # User profile
│           ├── admin_panel.html            # Admin moderation panel
│           ├── css/                        # Stylesheets
│           ├── js/                         # JavaScript files
│           ├── images/                     # Static assets
│           └── WEB-INF/
│               ├── web.xml                 # Servlet configuration (Servlet 6.0)
│               └── lib/
│                   └── mysql-connector-j-8.0.33.jar
└── build/
    └── classes/                            # Compiled .class files
```

---

## ⚙️ Prerequisites

- Java 21+
- Apache Tomcat 11
- MySQL 8+
- Eclipse IDE (with Eclipse Web Tools Platform)

---

## 🚀 Getting Started

### 1. Clone / Import the Project

Import the project into Eclipse:
**File → Import → Existing Projects into Workspace** → select the `NoteSphere` folder.

### 2. Configure Tomcat 11 in Eclipse

Go to **Window → Preferences → Server → Runtime Environments → Add** and select **Apache Tomcat v11.0**, then point it to your Tomcat 11 installation directory.

Then right-click the project → **Properties → Targeted Runtimes** → check ✅ **Apache Tomcat v11.0**.

### 3. Set Up the Database

Open MySQL and run the following:

```sql
CREATE DATABASE NoteSphere;
USE NoteSphere;

CREATE TABLE uploads (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(255)  NOT NULL,
    subject      VARCHAR(255),
    year         VARCHAR(50),
    unit         VARCHAR(50),
    file_path    VARCHAR(500),
    uploaded_by  VARCHAR(255),
    status       ENUM('pending', 'approved', 'rejected') DEFAULT 'pending',
    uploaded_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 4. Configure Database Credentials

Open `src/main/java/website/DBConnection.java` and update:

```java
private static final String URL      = "jdbc:mysql://localhost:3306/NoteSphere";
private static final String USERNAME = "your_mysql_username";
private static final String PASSWORD = "your_mysql_password";
```

> 💡 Default port is `3306`. Update to `3307` if your MySQL uses a non-standard port.

### 5. Build & Run

- **Project → Clean** to recompile
- Right-click project → **Run As → Run on Server** → select your Tomcat 11 instance
- Open your browser and navigate to:

```
http://localhost:8080/NoteSphere/
```

---

## 🔑 Google OAuth Setup

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project and enable the **Google Identity** API
3. Create OAuth 2.0 credentials (Web Application)
4. Add `http://localhost:8080` to **Authorized JavaScript origins**
5. Copy your **Client ID** and paste it into the relevant HTML pages where `google_signin` is configured

---

## 📡 API Endpoints

| Method | URL | Description |
|---|---|---|
| `GET` | `/AdminServlet?status=all` | Fetch all uploads |
| `GET` | `/AdminServlet?status=pending` | Fetch pending uploads |
| `POST` | `/AdminServlet?action=approve&id={id}` | Approve an upload |
| `POST` | `/AdminServlet?action=reject&id={id}` | Reject an upload |
| `POST` | `/UploadServlet` | Upload a new file (multipart) |
| `GET` | `/uploads/{filename}` | Serve an uploaded file |

---

## 📝 Upload Limits

| Setting | Limit |
|---|---|
| Max file size | 20 MB |
| Max request size | 25 MB |
| File threshold (memory) | 1 MB |

---

## 🛡️ Notes & Known Limitations

- Files are stored **locally** on the server under the `uploads/` directory inside the deployment folder
- There is no user registration system — authentication relies entirely on Google Sign-In
- Admin credentials are not managed through the database — consider adding a session-based admin login for production use
- CORS is set to `*` on all servlets — restrict this in production

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m 'Add your feature'`
4. Push to the branch: `git push origin feature/your-feature`
5. Open a Pull Request

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).

---

<p align="center">Made with ❤️ for students, by students.</p>
