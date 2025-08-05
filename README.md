# RTLVod Android App

## 📱 Overview

RTLVod is a native Android app that allows authenticated users to log in and stream rugby videos hosted on [https://test.rtlvod.com](https://test.rtlvod.com). It uses secure login, dynamic video list loading, and smooth video playback via ExoPlayer. The app is designed for cast-enabled smart TVs but does not require Chromecast support.

---

## 🎯 Features

- 🔐 **Login System** – Secure login with username and password
- 🎞️ **Video Listing** – Fetches videos dynamically from the backend
- ▶️ **Video Playback** – Plays videos with ExoPlayer in full screen
- 🌐 **HTTPS API** – Communicates with a secure backend at `https://test.rtlvod.com:3001`
- 📦 **Modular Code** – Clean separation of concerns using Retrofit, data models, and activity-based navigation
- 🚫 **No Chromecast Dependency** – Designed for native TV casting support

---

## 🔧 Technologies Used

| Component     | Technology         |
|---------------|--------------------|
| Language      | Kotlin             |
| Backend Comm  | Retrofit2 + GSON   |
| Video Player  | ExoPlayer          |
| UI Toolkit    | Android XML Views  |
| Min SDK       | 21                 |
| Target SDK    | 34                 |

---

## 📂 Project Structure

RTLVodAppComplete/
├── app/
│ ├── java/com/example/rtlvodapp/
│ │ ├── LoginActivity.kt
│ │ ├── VideoListActivity.kt
│ │ ├── VideoPlayerActivity.kt
│ │ ├── model/
│ │ │ ├── LoginRequest.kt
│ │ │ ├── LoginResponse.kt
│ │ │ └── VideoItem.kt
│ │ └── network/
│ │ ├── ApiClient.kt
│ │ └── ApiService.kt
│ ├── res/layout/
│ │ ├── activity_main.xml
│ │ ├── activity_video_list.xml
│ │ └── activity_video_player.xml
│ └── AndroidManifest.xml


---

## 🛠️ Setup Instructions

### 🔃 1. Clone the Repo

```bash
git clone https://github.com/yourusername/RTLVodAppComplete.git
cd RTLVodAppComplete

🧰 2. Open in Android Studio
Open Android Studio.

Choose File > Open and select this folder.

⚙️ 3. Configure API Endpoint (if different)
Update the baseUrl in ApiClient.kt if your backend URL changes:
.baseUrl("https://test.rtlvod.com:3001/")

▶️ 4. Run the App
Build and run on a physical Android device or emulator.

Log in using the test credentials:
Username: testadmin
Password: test123

🧱 Backend Integration
The app communicates with the backend hosted at:
https://test.rtlvod.com:3001

Nginx reverse proxy handles HTTPS with Let’s Encrypt certificates, serving both frontend and API.

Example Nginx config:
location /api/ {
    proxy_pass http://localhost:5001;
}

The API must:
Accept POST requests to /login with JSON body
Return a token and success status
Provide GET /api/videos that returns a list of video metadata

| Error                                | Solution                                                        |
| ------------------------------------ | --------------------------------------------------------------- |
| `Hostname not verified`              | Ensure you're using the domain (`test.rtlvod.com`) not raw IP.  |
| `controller_auto_show not found`     | Use correct ExoPlayer attributes and exclude unsupported ones.  |
| `Unresolved reference: LoginRequest` | Ensure files are in the correct `model` and `network` packages. |
