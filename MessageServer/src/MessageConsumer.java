import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Queue;

public class MessageConsumer extends Thread {
    @Override
    public void run() {
        System.out.println("Message consumer started...");

        while (true) {
            try {
                Queue<Message> messages = Server.getMessages();
                Message message = messages.poll();
                Server.setMessages(messages);

                if (message != null) {
                    consumeMessage(message);
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    private void consumeMessage(Message message) {
        try {
            User destinationUser = message.getDestinationUser();
            DataOutputStream outputStream = new DataOutputStream(Server.getConnectedClients().get(destinationUser).getOutputStream());
            outputStream.writeUTF(message.toString());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}