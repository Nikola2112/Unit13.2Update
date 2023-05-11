import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

class JsonPlaceholderApiClient {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private Gson gson;

    public JsonPlaceholderApiClient() {
        gson = new GsonBuilder().create();
    }

    public static void main(String[] args) {
        JsonPlaceholderApiClient client = new JsonPlaceholderApiClient();

        client.createUser();
        client.updateUser();
        client.deleteUser();
        client.getAllUsers();
        client.getUserById(1);
        client.getUserByUsername("Bret");
        client.getAndSaveCommentsForLastPost(1, 10);
        client.getOpenTasksForUser(1);
    }


    public void createUser() {
        try {
            URL url = new URL(BASE_URL + "/users");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            User newUser = new User("John Doe", "johndoe@example.com");
            String jsonInputString = gson.toJson(newUser);

            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                outputStream.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    User createdUser = gson.fromJson(response.toString(), User.class);
                    System.out.println("Создан новый пользователь: " + createdUser);
                }
            } else {
                System.out.println("Ошибка при создании пользователя. Код ответа: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateUser() {
        try {
            URL url = new URL(BASE_URL + "/users/1");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            User updatedUser = new User(1, "Updated Name", "johndoe@example.com");
            String jsonInputString = gson.toJson(updatedUser);

            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                outputStream.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    User user = gson.fromJson(response.toString(), User.class);
                    System.out.println("Обновленный пользователь: " + user);
                }
            } else {
                System.out.println("Ошибка при обновлении пользователя. Код ответа: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser() {
        try {
            URL url = new URL(BASE_URL + "/users/1");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Користувача успішно видалено.");
            } else {
                System.out.println("Помилка під час видалення користувача. Код відповіді: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAllUsers() {
        try {
            URL url = new URL(BASE_URL + "/users");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    User[] users = gson.fromJson(response.toString(), User[].class);
                    for (User user : users) {
                        System.out.println(user);
                    }
                }
            } else {
                System.out.println("Помилка отримання списку користувачів. Код відповіді: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getUserById(int id) {
        try {
            URL url = new URL(BASE_URL + "/users/" + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    User user = gson.fromJson(response.toString(), User.class);
                    System.out.println("Користувач за ідентифікатором " + id + ": " + user);
                }
            } else {
                System.out.println("Помилка отримання користувача за ідентифікатором. Код відповіді: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getUserByUsername(String username) {
        try {
            URL url = new URL(BASE_URL + "/users?username=" + username);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    User[] users = gson.fromJson(response.toString(), User[].class);
                    if (users.length > 0) {
                        System.out.println("Користувач за ім'ям користувача " + username + ": " + users[0]);
                    } else {
                        System.out.println("Користувача за ім'ям користувача " + username + " не знайдено.");
                    }
                }
            } else {
                System.out.println("Помилка отримання користувача за ім'ям користувача. Код відповіді: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAndSaveCommentsForLastPost(int userId, int postId) {
        try {
            // Получение последнего поста пользователя
            URL userPostsUrl = new URL(BASE_URL + "/users/" + userId + "/posts");
            HttpURLConnection userPostsConnection = (HttpURLConnection) userPostsUrl.openConnection();
            userPostsConnection.setRequestMethod("GET");

            int userPostsResponseCode = userPostsConnection.getResponseCode();
            if (userPostsResponseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(userPostsConnection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    Post[] posts = gson.fromJson(response.toString(), Post[].class);
                    if (posts.length > 0) {
                        Post lastPost = posts[posts.length - 1];

                        // Получение комментариев к последнему посту
                        URL postCommentsUrl = new URL(BASE_URL + "/posts/" + lastPost.getId() + "/comments");
                        HttpURLConnection postCommentsConnection = (HttpURLConnection) postCommentsUrl.openConnection();
                        postCommentsConnection.setRequestMethod("GET");

                        int postCommentsResponseCode = postCommentsConnection.getResponseCode();
                        if (postCommentsResponseCode == HttpURLConnection.HTTP_OK) {
                            try (BufferedReader commentsReader = new BufferedReader(new InputStreamReader(postCommentsConnection.getInputStream()))) {
                                StringBuilder commentsResponse = new StringBuilder();
                                String commentsLine;
                                while ((commentsLine = commentsReader.readLine()) != null) {
                                    commentsResponse.append(commentsLine);
                                }

                                Comment[] comments = gson.fromJson(commentsResponse.toString(), Comment[].class);
                                String fileName = "user-" + userId + "-post-" + postId + "-comments.json";
                                saveCommentsToFile(comments, fileName);
                                System.out.println("Комментарии к последнему посту пользователя сохранены в файл: " + fileName);
                            }
                        } else {
                            System.out.println("Ошибка при получении комментариев к последнему посту. Код ответа: " + postCommentsResponseCode);
                        }
                    } else {
                        System.out.println("У пользователя нет постов.");
                    }
                }
            } else {
                System.out.println("Ошибка при получении постов пользователя. Код ответа: " + userPostsResponseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCommentsToFile(Comment[] comments, String fileName) {
        try (Writer writer = new FileWriter(fileName)) {
            gson.toJson(comments, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getOpenTasksForUser(int userId) {
        try {
            URL url = new URL(BASE_URL + "/users/" + userId + "/todos");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    Todo[] todos = gson.fromJson(response.toString(), Todo[].class);
                    for (Todo todo : todos) {
                        if (!todo.isCompleted()) {
                            System.out.println("Задача для пользователя " + userId + ": " + todo.getTitle());
                        }
                    }
                }
            } else {
                System.out.println("Ошибка при получении списка задач для пользователя. Код ответа: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
