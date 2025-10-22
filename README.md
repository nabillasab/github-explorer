# github-user
This project contains list of github users including their repositories.

### API Documentation

| Feature | Documentation |
|----------|----------|
| Github User List    | [API](https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#list-users)     |
| Github User Detail    |  [API](https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#get-a-user)   |
| Search User    | [API](https://docs.github.com/en/rest/search/search?apiVersion=2022-11-28#search-users)     |
| List of Repositories of Specific User    | [API](https://docs.github.com/en/rest/repos/repos?apiVersion=2022-11-28#list-repositories-for-a-user)     |

### Tech Stack
- Kotlin
- MVVM
- Jetpack Compose
- Coroutine
- Clean Architecture
- Coil
- Paging 3
- Hilt
- Room

### Features
1. User List Screen
   - Minimum Requirement:
     - show list of users
     - search user
     - click on user item

2. User Repository Screen
   - Minimum Requirement:
     - show user's detail
     - show user's repository list
     - click on repository item

  Additional Features:
  - loading
  - error handling when response API error
  - error no internet connection etc
  - pagination for user list and repository list
  - save and load from local db using room to reduce too much hit the API for user list and repository list
  - unit test

3. Repository Web Page Screen (Webview)
