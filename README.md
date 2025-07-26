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

2. ### Build the Project with Maven

```bash
mvn clean install
```

3. ### Run the Application (JavaFX GUI)
```
mvn javafx:run
````

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
