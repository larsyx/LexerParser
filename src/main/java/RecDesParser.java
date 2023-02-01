import java.io.IOException;


public class RecDesParser {

    static int ptr;
    static int ptrtmp;
    static char[] input;

    public static void main(String[] args) throws IOException {

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
        //System.out.println(s);

        input = s.toCharArray();
        if(input.length < 2) {
            System.out.println("Syntax error");
            System.exit(0);
        }
        ptr = 0;
        boolean isValid = S();
        if((isValid) & (ptr == input.length)) {
            System.out.println("Input is valid");
        } else {
            System.out.println("syntax error");
        }
    }

    public static boolean S() {
        // Check if 'S' --> program EOF
        int fallback = ptr;
        if(!program()) {
            ptr = fallback;
            return false;
        }
        if(input[ptr++] != 'E' || input[ptr++] != 'O' || input[ptr++] != 'F' ) {
            ptr = fallback;
            return false;
        }
        return true;
    }

    public static boolean program() {
        // Check if Program --> Stmt Program1
        int fallback = ptr;
        if(!stmt()) {
            ptr = fallback;
            return false;
        }
        if(!program1()) {
            ptr = fallback;
            return false;
        }

        return true;
    }

    public static boolean program1() {
        // Check if Program1 -> ; Stmt Program1
        int fallback = ptr;
        ptrtmp = ptr;
        if( input[ptr++] == 'S' && input[ptr++] == 'E' && input[ptr++] == 'M' && input[ptr++] == 'I' ) {
            if(!stmt()) {
                ptr = fallback;
                return false;
            }
            if(!program1()) {
                ptr = fallback;
                return false;
            }
            return true;
        }
        ptr = ptrtmp;
        // Check Program1 -> ε
        return true;
    }

    public static boolean stmt() {
        int fallback = ptr;

        // Check if Stmt -> IF Expr THEN Stmt END IF
        ptrtmp = ptr;
        if(input[ptr++] == 'I' && input[ptr++] == 'F' ){
            if(!expr()){
                ptr=fallback;
                return false;
            }
            if(input[ptr++] != 'T' || input[ptr++] != 'H' || input[ptr++] != 'E' || input[ptr++] != 'N'){
                ptr = fallback;
                return false;
            }

            if(!stmt()){
                ptr=fallback;
                return false;
            }
            ptrtmp = ptr;
            // Check if Stmt -> IF Expr THEN Stmt ELSE Stmt END IF
            if(input[ptr++] == 'E' && input[ptr++] == 'L' && input[ptr++] == 'S' && input[ptr++] == 'E'){
                if(!stmt()){
                    ptr=fallback;
                    return false;
                }
            }
            else
                ptr = ptrtmp;

            if(input[ptr++] != 'E' || input[ptr++] != 'N' || input[ptr++] != 'D' || input[ptr++] != 'I' || input[ptr++] != 'F'){
                ptr = fallback;
                return false;
            }

            return true;
        }
        // Check if Stmt -> ID ASSIGN Expr
        else{
            ptr = ptrtmp;
            if(input[ptr++] == 'I' && input[ptr++] == 'D' ){
                if(input[ptr++] != 'A' || input[ptr++] != 'S' || input[ptr++] != 'S' || input[ptr++] != 'I' || input[ptr++] != 'G' || input[ptr++] != 'N'){
                    ptr = fallback;
                    return false;
                }
                if(!expr()){
                    ptr=fallback;
                    return false;
                }
                return true;
            }
            // Check if Stmt -> WHILE Expr LOOP Stmt END LOOP
            else {
                ptr=ptrtmp;
                if (input[ptr++] != 'W' || input[ptr++] != 'H' || input[ptr++] != 'I' || input[ptr++] != 'L' || input[ptr++] != 'E') {
                    ptr = fallback;
                    return false;
                }
                if (!expr()){
                    ptr = fallback;
                    return false;
                }

                if (input[ptr++] != 'L' || input[ptr++] != 'O' || input[ptr++] != 'O' || input[ptr++] != 'P') {
                    ptr = fallback;
                    return false;
                }

                if (!stmt()){
                    ptr = fallback;
                    return false;
                }

                if (input[ptr++] != 'E' || input[ptr++] != 'N' || input[ptr++] != 'D' || input[ptr++] != 'L' || input[ptr++] != 'O' || input[ptr++] != 'O' || input[ptr++] != 'P'){
                    ptr = fallback;
                    return false;
                }
                return true;
            }
        }
    }

    public static boolean expr() {
        int fallback = ptr;
        ptrtmp = ptr;
        if(input[ptr++] == 'I' && input[ptr++] == 'D' ) {
            if(!expr1()){
                ptr = fallback;
                return false;
            }
            return true;
        }
        else {
            ptr=ptrtmp;
            if (input[ptr++] != 'N' || input[ptr++] != 'U' || input[ptr++] != 'M' || input[ptr++] != 'B' || input[ptr++] != 'E' || input[ptr++] != 'R') {
                ptr=fallback;
                return false;
            }

            if(!expr1()){
                ptr = fallback;
                return false;
            }
            return true;
        }
    }

    public static boolean expr1() {
        int fallback = ptr;

        // Check if Stmt -> RELOP Expr Expr1
        ptrtmp = ptr;
        if(input[ptr++] == 'R' && input[ptr++] == 'E' && input[ptr++] == 'L' && input[ptr++] == 'O' && input[ptr++] == 'P') {
            if(!expr()){
                ptr = fallback;
                return false;
            }
            if(!expr1()){
                ptr = fallback;
                return false;
            }
        }
        ptr = ptrtmp;
        // Check if Stmt -> ε
        return true;
    }
}

