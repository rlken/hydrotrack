# 💧 HydroTrack — Complete Setup Guide (Mac)

This guide will walk you through every step from a fresh Mac to a running HydroTrack application.

---

## Step 1: Install Homebrew (Mac Package Manager)

Open **Terminal** (search "Terminal" in Spotlight) and paste this command:

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

It will ask for your **Mac password** (the one you use to log in). Type it and press Enter.

> [!IMPORTANT]
> After it finishes, it will show **"Next Steps"** at the bottom. You MUST run those commands to add Homebrew to your PATH. They look something like:
> ```bash
> echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> ~/.zprofile
> eval "$(/opt/homebrew/bin/brew shellenv)"
> ```
> Copy and run both lines exactly as shown in your terminal.

**Verify it works:**
```bash
brew --version
```
You should see something like `Homebrew 4.x.x`.

---

## Step 2: Install Java 17

```bash
brew install openjdk@17
```

Then link it so your system can find it:
```bash
sudo ln -sfn $(brew --prefix openjdk@17)/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk
```

Add it to your PATH:
```bash
echo 'export PATH="$(brew --prefix openjdk@17)/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

**Verify:**
```bash
java -version
```
Should show `openjdk version "17.x.x"`.

---

## Step 3: Install Maven (Build Tool)

```bash
brew install maven
```

**Verify:**
```bash
mvn -version
```
Should show `Apache Maven 3.x.x`.

---

## Step 4: Install MySQL 8

```bash
brew install mysql
```

### Start MySQL server:
```bash
brew services start mysql
```

### Secure your installation (set root password):
```bash
mysql_secure_installation
```

This will ask you several questions:
1. **VALIDATE PASSWORD component** → Type `n` (no) — keeps it simple
2. **New password for root** → Type a password you'll remember (e.g., `hydrotrack2025`)
3. **Re-enter password** → Same password
4. **Remove anonymous users?** → `y`
5. **Disallow root login remotely?** → `y`
6. **Remove test database?** → `y`
7. **Reload privilege tables?** → `y`

> [!CAUTION]
> **Remember your root password!** You'll need it in the next steps.

**Verify MySQL is running:**
```bash
mysql -u root -p
```
Type your password. You should see the `mysql>` prompt. Type `exit` to leave.

---

## Step 5: Create the HydroTrack Database

Log into MySQL:
```bash
mysql -u root -p
```

Then run these commands one at a time inside the `mysql>` prompt:

```sql
CREATE DATABASE hydrotrack;
USE hydrotrack;
```

Type `exit` to leave MySQL for now.

---

## Step 6: Configure the Project

Open the application properties file in a text editor:

```bash
nano /Users/rken/.gemini/antigravity/scratch/hydrotrack/src/main/resources/application.properties
```

Find this line:
```
spring.datasource.password=your_password_here
```

Change `your_password_here` to **your MySQL root password** (the one from Step 4).

**Save and exit nano:** Press `Ctrl + X`, then `Y`, then `Enter`.

---

## Step 7: Run the Application!

Navigate to the project and start it:

```bash
cd /Users/rken/.gemini/antigravity/scratch/hydrotrack
mvn spring-boot:run
```

> [!NOTE]
> The first time you run this, Maven will download all dependencies. This can take **2-5 minutes** depending on your internet speed. Be patient!

When you see this line in the output, it means the app is running:
```
Started HydroTrackApplication in X.XX seconds
```

---

## Step 8: Open in Browser

Open your browser and go to:

**👉 http://localhost:8080**

You'll see the HydroTrack login page!

### Login credentials:

| Username | Password | Role | Can do |
|---|---|---|---|
| `admin` | `admin123` | Admin | Everything |
| `tech.juan` | `password123` | Technician | Manage fountains, schedules, maintenance |
| `insp.maria` | `password123` | Inspector | Record water quality |
| `viewer.jose` | `password123` | Viewer | View dashboard & data only |

---

## Step 9: Load Advanced Database Features

While the app is running (in a **new terminal tab/window** — press `Cmd + T`), run these SQL scripts:

```bash
mysql -u root -p hydrotrack < /Users/rken/.gemini/antigravity/scratch/hydrotrack/sql/views.sql
mysql -u root -p hydrotrack < /Users/rken/.gemini/antigravity/scratch/hydrotrack/sql/procedures.sql
mysql -u root -p hydrotrack < /Users/rken/.gemini/antigravity/scratch/hydrotrack/sql/triggers.sql
```

Each command will ask for your MySQL password. These install the:
- ✅ 5 database views
- ✅ 3 stored procedures + 2 functions
- ✅ 5 triggers

---

## Step 10: Set Up GitHub for Your Team

### Install Git (if not installed):
```bash
brew install git
```

### Initialize the repository:
```bash
cd /Users/rken/.gemini/antigravity/scratch/hydrotrack
git init
git add .
git commit -m "Initial HydroTrack project"
```

### Create a GitHub repository:
1. Go to [github.com/new](https://github.com/new)
2. Name it `hydrotrack`
3. Set to **Private**
4. Do NOT initialize with README (we already have one)
5. Click **Create repository**

### Push to GitHub:
```bash
git remote add origin https://github.com/YOUR-USERNAME/hydrotrack.git
git branch -M main
git push -u origin main
```

### Invite your teammates:
1. Go to your repo on GitHub
2. Click **Settings** → **Collaborators** → **Add people**
3. Add your team members by their GitHub usernames or emails

---

## Step 11: What Your Teammates Need to Do

Once your teammates accept the GitHub invitation, they do NOT need to do all the steps above. Send them these simplified instructions:

### Step 11a: Install Prerequisites

**For Mac Users:**
Run Step 1 to Step 4 from above (Homebrew, Java, Maven, MySQL).

**For Windows Users:**
Since Windows doesn't have Homebrew, they need to download and install these manually:
1. **Java 17**: Download the Windows x64 Installer from [Oracle's website](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) and run it.
2. **Maven**: Download the zip from [Apache Maven](https://maven.apache.org/download.cgi), extract it to `C:\Maven`, and add `C:\Maven\bin` to their Windows System Environment Variables (`PATH`).
3. **MySQL 8**: Download the [MySQL Installer for Windows](https://dev.mysql.com/downloads/installer/) and run the default installation, making sure to remember the root password they create.
4. **Git**: Download and install [Git for Windows](https://gitforwindows.org/). This gives them "Git Bash", which they should use instead of the normal Windows Command Prompt for all terminal commands below.

### Step 11b: Setup the Project

*(Windows users should run these commands in **Git Bash**)*

1. **Setup MySQL Database**:
   ```bash
   mysql -u root -p -e "CREATE DATABASE hydrotrack;"
   ```
2. **Clone the Repository**:
   ```bash
   git clone https://github.com/YOUR-USERNAME/hydrotrack.git
   cd hydrotrack
   ```
3. **Configure Password**: Edit `src/main/resources/application.properties` and change `spring.datasource.password` to match their own MySQL password.
4. **Load Advanced SQL Features** (Wait until the app runs at least once so tables are created):
   ```bash
   mysql -u root -p hydrotrack < sql/views.sql
   mysql -u root -p hydrotrack < sql/procedures.sql
   mysql -u root -p hydrotrack < sql/triggers.sql
   ```
5. **Run the App**:
   ```bash
   mvn spring-boot:run
   ```

---

## ❓ Troubleshooting

### "Port 8080 already in use"
```bash
lsof -i :8080
kill -9 <PID>
```

### "Access denied for user 'root'"
Double-check your password in `application.properties` matches your MySQL root password.

### "Cannot connect to MySQL server"
Make sure MySQL is running:
```bash
brew services start mysql
```

### To stop the application
Press `Ctrl + C` in the terminal where the app is running.

### To stop MySQL
```bash
brew services stop mysql
```
