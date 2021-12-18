import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;

public class ClientThread extends Thread {
    private Socket socket;
    private User user;

    public ClientThread(User user, Socket socket) {
        this.user = user;
        this.socket = socket;
    }

    @Override
    public void run() {
        DataInputStream inputStream;
        DataOutputStream outputStream;

        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }

        String str;
        while (true) {
            try {
                str = inputStream.readUTF();

                if (str.startsWith("/chat")) {
                    String[] args = str.split("/", 4);
                    String destinationUsername = args[2];
                    User destinationUser = Server.getUserByUsername(destinationUsername);
                    String messageText = args[3];

                    if (Server.isUserConnected(destinationUser)) {
                        Message message = new Message(messageText, user, destinationUser);
                        Queue<Message> messages = Server.getMessages();
                        messages.add(message);
                        Server.setMessages(messages);
                    } else {
                        outputStream.writeUTF("The client is not connected...");
                    }
                }
                else if (str.startsWith("/topic")) {
                    String[] args = str.split("/", 5);
                    String topic = args[2];
                    ConcurrentHashMap<String, List<Topic>> topics = Server.getTopics();

                    if (args.length == 3) {
                        List<Topic> messages = topics.get(topic);

                        if (messages != null) {
                            StringBuilder sb = new StringBuilder();

                            messages = messages.stream()
                                    .filter(s -> !s.isExpired())
                                    .collect(Collectors.toList());

                            topics.put(topic, messages);
                            Server.setTopics(topics);

                            sb.append(topic);
                            sb.append(":\n");
                            for (Topic message : messages) {
                                sb.append(message.getMessage());
                                sb.append("\n");
                            }

                            outputStream.writeUTF(sb.toString());
                            outputStream.flush();
                        }
                    }
                    else if (args.length == 5) {
                        int timeout = Integer.parseInt(args[3]);
                        String message = args[4];

                        Topic postedTopic = new Topic(topic, timeout, message);
                        List<Topic> messages;

                        if (topics.containsKey(postedTopic.getCategory())) {
                            messages = topics.get(postedTopic.getCategory());
                        }
                        else {
                            messages = new ArrayList<>();
                        }

                        messages.add(postedTopic);
                        topics.put(postedTopic.getCategory(), messages);
                        Server.setTopics(topics);

                        outputStream.writeUTF("Message posted on topic " + postedTopic.getCategory());
                    }
                }
                else if (str.equals("/exit")) {
                    socket.close();
                    break;
                }
            } catch (IOException e) {
                Server.removeClient(user);
                break;
            }
        }
        try {
            socket.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

