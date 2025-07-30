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

<img width="1919" height="1033" alt="image" src="https://github.com/user-attachments/assets/11b72a58-a184-4bd1-8cd6-a06d5ecf8c4a" />

Maven Setup Video: <br>
https://www.youtube.com/watch?v=XEphzGQz-nI


3. ### Build the Project with Maven

```bash
mvn clean install
```

4. ### Run the Application (JavaFX GUI)
```
mvn javafx:run
````


5. ### 🧰 How to Use JavaFX Scene Builder in VS Code

## Scene Builder Setup

1. **Download Scene Builder**: https://gluonhq.com/products/scene-builder/
2. **Install** Scene Builder
3. **Add Scene Builder to PATH** (C:\SceneBuilder\) And **Update tasks.json with your path** <br>
> 🔴 **IMPORTANT:** When installing Scene Builder, choose this path during setup:  
> `C:\SceneBuilder\`

Scene Builder install guide video: <br>
https://www.youtube.com/watch?v=IZCwawKILsk

6. ### 🧩 Link Scene Builder to VS Code
```
1. Open VS Code
2. Go to Extensions tab
3. Find: JavaFX Support and After install that Extension
```

7. ### Check tasks.json
If Scene Builder is not in PATH, update `.vscode/tasks.json`:
- Find tasks labeled "Open login.fxml in Scene Builder" and "Open Current FXML in Scene Builder"
- Replace `""SceneBuilder path"` with your correct path like:
  ```json
  "command": "C:\\SceneBuilder\\SceneBuilder.exe"
  ```


8. ### Running the Application

🔹 **Build**: `Ctrl+Shift+P` → "Tasks: Run Task" → "maven: package with dependencies" <br>
🔹 **Run**: `F5` or `Ctrl+Shift+P` → "Debug: Start Debugging" <br>
🔹 **Edit FXML**: `Ctrl+Shift+P` → "Tasks: Run Task" → "Open login.fxml in Scene Builder" <br>


9. ### Finnaly When you need, You Can Run Project using Debugging Option
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

### Troubleshooting

## JavaFX Runtime Missing Error
- Ensure dependencies are copied: Run "maven: package with dependencies" task first
- Check launch configuration has proper vmArgs

## Scene Builder Not Opening
- Verify Scene Builder installation path in tasks.json
- Ensure Scene Builder is properly installed


🤝 Contributing
Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.
