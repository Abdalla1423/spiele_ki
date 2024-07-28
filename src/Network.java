import java.io.*;
import java.net.*;

class Network {
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private String server = "localhost";
    private int port = 5555; // Port on which the server.py is running
    private String p; // Player number

    /**
     * Constructor that establishes a connection to the server.
     */
    public Network() {
        try {
            client = new Socket(server, port);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            p = readPlayerNumber(); // Read the player number from the server
            // p = in.readLine(); // TODO: use this if python server.py is changed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the player number from the server.
     *
     * @return The player number as a string.
     */
    private String readPlayerNumber() {
        char value;
        String tmp = "";
        try {
            value = (char) in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tmp += value;
        return tmp;
    }

    /**
     * Reads the response from the server until the end of the JSON object is reached.
     *
     * @return The server response as a string.
     */
    private String readResponse() {
        int value;
        StringBuilder tmp = new StringBuilder();
        while (true) {
            try {
                value = in.read();
                tmp.append((char) value);
                if ((char) value == '}') {
                    break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return tmp.toString();
    }

    /**
     * Gets the player number.
     *
     * @return The player number as a string.
     */
    public String getP() {
        return p;
    }

    /**
     * Sends data to the server and receives the response.
     *
     * @param data The data to be sent to the server.
     * @return The response from the server as a string.
     */
    public String send(String data) {
        try {
            out.println(data);
            return readResponse(); // Read the response from the server
            // return in.readLine(); // TODO: use this if python server.py is changed
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
