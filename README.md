# GitHub Explorer
An Android app that lets you search and explore GitHub users and view their repositories.

![Platform](https://img.shields.io/badge/platform-Android-green)
![Language](https://img.shields.io/badge/language-Kotlin-blue)

## 📱 Screenshots
<p float="left">
   <img width="280" alt="user_list" src="https://github.com/user-attachments/assets/7c3f63a3-458d-4635-83ea-3c9f1fbfaaf0" />
   <img width="280" alt="user_detail" src="https://github.com/user-attachments/assets/045f502a-bdcb-4b71-81bf-bf8ccba71ddc" />
</p>

## ✨ Features
- 🔍 Search GitHub users by name
- 👤 View user profiles and repositories
- ☘️ Repository Detail Web Page Screen (Webview)
- ⭐ Display repo stats (stars, forks, last updated, popular repo)
- 🧱 Built with MVVM, Retrofit, Hilt, and Jetpack components

## 🧩 Tech Stack
- **Language:** Kotlin  
- **Architecture:** MVVM, Hilt
- **Networking:** Retrofit + Coroutines + Flow
- **Image Loading:** Coil  
- **UI:** Jetpack Compose
- **Database & Caching:** Room, Paging 3
- **Continuous Integration:** Jenkins
- **Test Coverage Report:** JaCoCo

## 🚀 Getting Started
1. Clone the repo:
   ```bash
   git clone https://github.com/nabillasab/github-explorer.git
   ```
2. Open in Android Studio
3. Add your [fine-grain personal access token Github](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#creating-a-fine-grained-personal-access-token) in local.properties for variable `github_auth_token`
4. Run on an emulator or device!

## 💾 GitHub API

| Feature | Documentation |
|----------|----------|
| Github User List    | [API](https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#list-users)     |
| Github User Detail    |  [API](https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#get-a-user)   |
| Search User    | [API](https://docs.github.com/en/rest/search/search?apiVersion=2022-11-28#search-users)     |
| List of Repositories of Specific User    | [API](https://docs.github.com/en/rest/repos/repos?apiVersion=2022-11-28#list-repositories-for-a-user)     |

## 🙏 Acknowledgements
- Data provided by [GitHub API](https://docs.github.com/en/rest)
- Icons by [Flaticon](https://flaticon.com)
