import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static final int PORT = 8081;
    private static int usersCounter = 0;
    private static Map<User, Socket> connectedClients = new HashMap<>();
    private static Queue<Message> messages = new ConcurrentLinkedQueue<>();
    private static ConcurrentHashMap<String, List<Topic>> topics = new ConcurrentHashMap<>();
    private static final int MAX_MESSAGE_TIMEOUT = 3600000;

    public static Queue<Message> getMessages() { return messages; }
    public static void setMessages(Queue<Message> messages) { Server.messages = messages; }
    public static ConcurrentHashMap<String, List<Topic>> getTopics() { return topics; }
    public static void setTopics(ConcurrentHashMap<String, List<Topic>> topics) { Server.topics = topics; }
    public static synchronized Map<User, Socket> getConnectedClients() { return connectedClients; }
    public static int getMaxMessageTimeout() { return MAX_MESSAGE_TIMEOUT; }

    public static boolean isUserConnected(User user) {
        for (Map.Entry<User, Socket> client : connectedClients.entrySet()) {
            User tempUser = client.getKey();
            if (tempUser.equals(user)) {
                return true;
            }
        }
        return false;
    }

    public static User getUserByUsername(String username) {
        for (Map.Entry<User, Socket> client : connectedClients.entrySet()) {
            User tempUser = client.getKey();
            if (tempUser.getUsername().equals(username)) {
                return tempUser;
            }
        }
        return null;
    }

    public static void removeClient(User user) {
        System.out.println(user.getUsername() + " left...");
        connectedClients.remove(user);
    }

    public static void main(String[] args) {
        ServerSocket serverSocket;
        Socket socket;

        try {
            serverSocket = new ServerSocket(PORT);
            new MessageConsumer().start();

            System.out.println("Server: Message consumer started.");

            while (true) {
                try {
                    socket = serverSocket.accept();

                    InputStreamReader inputStream = new InputStreamReader(socket.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStream);
                    String username = bufferedReader.readLine();
                    User newUser = new User(++usersCounter, username);

                    connectedClients.put(newUser, socket);
                    new ClientThread(newUser, socket).start();

                    System.out.println("Welcome " + username);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

