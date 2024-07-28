import com.google.gson.Gson;
import java.util.Scanner;

public class Client {

    /**
     * The main method that continuously runs the client.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        while (true) {
            runClient();
        }
    }

    /**
     * Runs the client to communicate with the server and play the game.
     */
    public static void runClient() {
        Network network = new Network();
        int player = Integer.parseInt(network.getP());
        System.out.println("You are player " + player);
        Gson gson = new Gson();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                // Send "get" request as a JSON to the server over the network
                Thread.sleep(60);
                String response = network.send(gson.toJson("get"));

                if (response == null) {
                    throw new ValueException("Game data is null");
                }

                // Transform JSON response to Java GameData object
                GameData game = gson.fromJson(response, GameData.class);

                // Only allow input when both players are connected
                if (game.bothConnected) {

                    // Allow input only when it is your turn
                    if (player == 0 && game.player1) {
                        // Helpful for debugging
                        System.out.println("New Board: " + game.board);

                        // Answer must have format: start_field-end_field like E7-F7
                        String input = Game.getMove(game.board, game.time);

                        // Transform the input move to JSON
                        String data = gson.toJson(input);

                        // Send data via network
                        network.send(data);
                    } else if (player == 1 && game.player2) {
                        System.out.println("New Board: " + game.board);
                        String input = Game.getMove(game.board, game.time);
                        String data = gson.toJson(input);
                        network.send(data);
                    }
                }
            } catch (Exception e) {
                System.out.println("Couldn't get game");
                break;
            }
        }
    }
}
