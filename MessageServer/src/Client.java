import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private static final int PORT = 1338;

    public static void main(String[] args) {
        try {
            InetAddress ip = InetAddress.getByName("localhost");
            Socket socket = new Socket(ip, PORT);

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                String str;
                String username;

                try {
                    System.out.print("Please insert username: ");

                    username = bufferedReader.readLine();
                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                    printWriter.println(username);
                    printWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                while (true) {
                    try {
                        str = bufferedReader.readLine();

                        if (str.startsWith("/chat") || str.startsWith("/topic")) {
                            outputStream.writeUTF(str);
                            outputStream.flush();
                        }
                        else if (str.equals("/exit")) {
                            socket.close();
                            break;
                        }
                    } catch (IOException t) {
                        t.printStackTrace();
                    }
                }
            }).start();

            while (true) {
                try {
                    String s = inputStream.readUTF();
                    System.out.println(s);
                } catch (IOException e) {
                    inputStream.close();
                    System.out.println("Server connection closed...");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

