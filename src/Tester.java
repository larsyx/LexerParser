import java.io.FileNotFoundException;
import java.io.IOException;

public class Tester {

    public static void main(String[] args) throws IOException {
        Lexer lexer=new Lexer();
        lexer.initialize(args[0]);

        for(Token token = lexer.nextToken(); token != null; token = lexer.nextToken()){
            System.out.println(token.toString());
        }
    }
}
