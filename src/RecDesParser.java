import java.io.IOException;
import java.util.Scanner;


public class RecDesParser {

    static int ptr;
    static char[] input;

    public static void main(String[] args) throws IOException {
        System.out.println("Inizio scansione");
        Lexer lexer=new Lexer();
        lexer.initialize(args[0]);

        String s="";
        boolean flag= true;
        for(Token token = lexer.nextToken(); flag; token = lexer.nextToken()){
            s += token.getTokenId();
            if(token.getTokenId().equals("EOF"))
                flag = false;
        }
        //test
        System.out.println(s);

        input = s.toCharArray();
        if(input.length < 2) {
            System.out.println("The input string is invalid.");
            System.exit(0);
        }
        ptr = 0;
        boolean isValid = S();
        if((isValid) & (ptr == input.length)) {
            System.out.println("The input string is valid.");
        } else {
            System.out.println("The input string is invalid.");
        }
    }

    static boolean S() {
        // Check if 'S' --> program EOF
        int fallback = ptr;
        if(program() == false) {
            ptr = fallback;
            return false;
        }
        if(input[ptr++] != 'E' || input[ptr++] != 'O' || input[ptr++] != 'F' ) {
            ptr = fallback;
            return false;
        }
        return true;
    }

    static boolean program() {
        return true;
    }

    static boolean stmt() {
        return true;
    }

    static boolean expr() {
        return true;
    }
}

