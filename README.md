# 🧾 Inventory Management System

A modern **desktop application** to manage product inventory for small to medium businesses or academic institutions. Built using **JavaFX** for GUI, **SQLite** for local database, and follows solid **Object-Oriented Programming (OOP)** principles for modular, maintainable code.

> 🎓 Second Year OOAD Group Project – Uva Wellassa University of Sri Lanka

---

## 🔧 Technologies Used

🔹 **Java 17** — Core application logic  
🔹 **JavaFX 21** — GUI (Graphical User Interface)  
🔹 **SQLite** — Embedded database  
🔹 **Gson** — JSON data handling (optional)  
🔹 **Maven** — Build tool and dependency manager  
🔹 **JUnit 5** — Unit testing  
🔹 **Git** — Version control

---

## 🚀 Features

✅ Login with Role-based Access (Admin / Staff)  
✅ Add, Update, Delete Inventory Items  
✅ Quantity adjustment with live visual stock alerts  
✅ Filter and Search products by name/category  
✅ Export Reports as **CSV** or **PDF**  
✅ Modular architecture following **OOP Principles**  
✅ Beautiful and simple **JavaFX GUI**  
✅ Fast and responsive (under 2 seconds for most actions)

---

## 🛠️ Getting Started for New Collaborators

Follow these steps to set up and run the project on your machine:

1. ### Clone the Repository

```bash
git clone https://github.com/logicsimplifier/inventory-management-system.git
cd inventory-management-system
```

2. ### Install Java & Maven
Make sure you have Java 17 and Maven installed.
🔹 Check Java version
```bash
java -version
```
🔹 Check Maven version
```bash
mvn -v
```
If not installed, download:

🔹Java: https://jdk.java.net/17/ <br>
🔹Maven: https://maven.apache.org/download.cgi


3. ### Build the Project with Maven

```bash
mvn clean install
```

4. ### Run the Application (JavaFX GUI)
```
mvn javafx:run
````


5. ### 🧰 How to Use JavaFX Scene Builder in VS Code
🖌️ 1. Download Scene Builder
Download from: <br>
🔗 https://gluonhq.com/products/scene-builder/

Install it normally (Windows .exe, macOS .dmg, or .deb for Linux).
(When install set file path -->  C:\SceneBuilder\)

Then your task.json file should have the follwing code lines:
```
        {
            "label": "Open login.fxml in Scene Builder",
            "type": "shell",
            "command": "C:\\SceneBuilder\\SceneBuilder.exe",
            "args": [
                "${workspaceFolder}/src/main/resources/fxml/view/login.fxml"
            ],
            "group": "build",
            "presentation": {
                "reveal": "never"
            },
            "problemMatcher": []
        },
        {
            "label": "Open Current FXML in Scene Builder",
            "type": "shell",
            "command": "C:\\SceneBuilder\\SceneBuilder.exe",
            "args": [
                "${file}"
            ],
            "group": "build",
            "presentation": {
                "reveal": "never"
            },
            "problemMatcher": []
        }

```

6. ### 🧩 Link Scene Builder to VS Code
```
1. Open VS Code
2. Go to Extensions tab
3. Find: JavaFX Support and After install that Extension
4. After Press Ctril+Shift+p to open Command Palette
5. Type: "Tasks: Run Task"
6. Open Current FXML in Scene Builder


```

7. Open Project using Debugging Option
```
click Run Tab
After Click Run Without Debugging
```

### 📂 Project Structure
```bash
📦 inventory-management
├── 📁 src
│   └── 📁 main
│       ├── 📁 java
│       │   └── 📁 com.inventoryapp
│       │       ├── Main.java               # JavaFX app launcher
│       │       ├── model/                  # Data models (Product, User)
│       │       ├── controller/             # UI controllers
│       │       ├── service/                # Business logic (ProductService, etc.)
│       │       └── util/                   # Utilities (DBUtils, ReportExporter)
│       └── 📁 resources
│           ├── 📁 css                      # JavaFX styles
│           ├── 📁 icons                    # App icons and images
│           └── 📁 fxml                     # FXML files for layouts (optional)
│
├── 📁 database
│   └── inventory.db                        # SQLite DB (if used)
├── 📄 pom.xml                              # Maven config file
├── 📄 .gitignore                           # Git ignore rules
├── 📄 README.md                            # You're reading it! 📘
└── 📄 LICENSE (optional)
```

### 🧪 Testing
Unit tests are written using JUnit 5.

To run all tests:

```bash
mvn test
````

🤝 Contributing
Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.
