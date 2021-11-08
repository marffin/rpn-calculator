import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RPNCalculator {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Expression expr = new Expression();

        String input;
        try {
            while ((input = br.readLine()) != null) {
                String[] tokens = input.trim().split(" ");
                if (tokens.length == 0)
                    continue;
                int pos = 1;
                try {
                    for (String token : tokens) {
                        if (token.length() == 0) {
                            pos += 1;
                            continue;
                        }
                        expr.feed(token, pos);
                        pos += (token.length() + 1);
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                System.out.println("stack: " + expr.formatStack());
            }
        } catch (IOException ex) {
            System.out.println("\nexit.");
        }
    }
}
