import java.io.*;
import java.util.HashMap;

public class Lexer {
    private File input;
    private BufferedReader reader;
    private static HashMap<String ,Token> tableSymbol;
    private int stato;
    private String lessema;
    private boolean flag;

    public Lexer(){
        stato=0;
        tableSymbol=new HashMap<>();
        lessema ="";
        flag = true;

        //inserimento keywords
        tableSymbol.put("if", new Token("IF"));
        tableSymbol.put("then", new Token("THEN"));
        tableSymbol.put("else", new Token("ELSE"));
        tableSymbol.put("while", new Token("WHILE"));

        tableSymbol.put(",", new Token("COMMA"));
        tableSymbol.put("\"", new Token("QUOTATIONMARKS"));
        tableSymbol.put(";", new Token("SEMICOLON"));

        tableSymbol.put("(", new Token("BRACKETROUNDOPEN"));
        tableSymbol.put(")", new Token("BRACKETROUNDCLOSE"));
        tableSymbol.put("[", new Token("BRACKETOPEN"));
        tableSymbol.put("]", new Token("BRACKETCLOSE"));
        tableSymbol.put("{", new Token("BRACEOPEN"));
        tableSymbol.put("}", new Token("BRACECLOSE"));

        tableSymbol.put("float", new Token("FLOAT"));
        tableSymbol.put("int", new Token("INT"));
        tableSymbol.put("bool", new Token("BOOLEAN"));
        tableSymbol.put("char", new Token("CHAR"));
        tableSymbol.put("String", new Token("STRING"));

        tableSymbol.put("+", new Token("OP", "+"));
        tableSymbol.put("-", new Token("OP", "-"));
        tableSymbol.put("/", new Token("OP", "/"));
        tableSymbol.put("*", new Token("OP", "*"));



    }

    public void initialize(String path) throws IOException {
        input= new File(path);
        if(!input.canRead())
            throw new FileNotFoundException("File non valido");
        else{
            reader = new BufferedReader(new FileReader(input));
        }
    }

    private static Token installId(String lessema){
        if(tableSymbol.containsKey(lessema))
            return tableSymbol.get(lessema);
        else{
            Token token= new Token("ID", lessema);
            tableSymbol.put(lessema, token);
            return token;
        }

    }

    public void retract(){
        try {
            //retract di massimo un carattere
            reader.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Token nextToken() {
        stato = 0;
        lessema = "";
        char carattere = 0;

        while (flag){
            switch (stato) {
                case 0:
                    carattere = readChar();
                    if(isBlank(carattere))
                        break;
                    lessema += carattere;
                    if (carattere == '<')
                        stato = 1;
                    else if(carattere == '!')
                        stato=3;
                    else if (carattere == '=')
                        stato=5;
                    else if (carattere == '>')
                        stato=6;
                    else if(Character.isDigit(carattere))  //numeri
                        stato=12;
                    else if(carattere == '-')    //numeri negativi
                        stato= 26;
                    else if(carattere == '/')    //commenti
                        stato= 27;
                    else {
                        if ( carattere == '\uFFFF') {  //fine stringa
                            flag = false;
                            new Token("EOF");
                        } else if(Character.isLetter(carattere))    //id
                            stato = 25;
                        else {
                            //caratteri singoli es. { } * +
                            Token t=installId(lessema);
                            resetLessema();
                            return t;
                        }
                    }
                    break;

                case 1:
                    carattere = this.readChar();
                    lessema += carattere;

                    if (carattere == '=')
                        stato = 2;
                    else
                        stato=4;
                    break;

                case 2:
                    return new Token("RELOP", "<=");

                case 3:
                    carattere = readChar();
                    if(carattere == '=')
                        return new Token("RELOP", "<>");
                    else
                        new Token("EOF");

                case 4:
                    retract();
                    lessema = lessema.substring(0, lessema.length() - 1);
                    return new Token("RELOP", "<");

                case 5:
                    return new Token("RELOP", "=");

                case 6:
                    carattere = readChar();
                    lessema += carattere;
                    if(carattere == '=')
                        stato = 7;
                    else
                        stato = 8;
                    break;

                case 7:
                    return new Token("RELOP", ">=");

                case 8:
                    if(!isBlank(carattere))
                        retract();
                    lessema = lessema.substring(0, lessema.length() - 1);
                    return new Token("RELOP", ">");

            //numeri
                case 26:
                    carattere = readChar();
                    if(Character.isDigit(carattere)) {
                        lessema += carattere;
                        stato=12;
                        break;
                    }
                    else {
                        if(carattere != '\uFFFF')
                            retract();
                        return installId(lessema);
                    }

                case 12:
                    carattere = readChar();
                    lessema += carattere;
                    if(carattere == '.') {
                        stato = 13;
                        break;
                    }
                    if(carattere == 'E') {
                        stato = 15;
                        break;
                    }
                    if(!Character.isDigit(carattere)) {
                        if(carattere != '\uFFFF')
                            retract();
                        lessema = lessema.substring(0, lessema.length() - 1);
                        return new Token("NUM", lessema);
                    }
                    break;

                case  13:
                    carattere = readChar();
                    lessema += carattere;
                    if (Character.isDigit(carattere)) {
                        stato = 14;
                        break;
                    }
                    else{
                        if(carattere != '\uFFFF')
                            retract();
                        lessema = lessema.substring(0, lessema.length() - 1);
                        return new Token("NUM", lessema);
                    }

                case 14:
                    carattere = readChar();
                    lessema += carattere;
                    if(Character.isDigit(carattere))
                        break;
                    if(carattere == 'E'){
                        stato=15;
                        break;
                    }else{
                        if(carattere != '\uFFFF')
                            retract();
                        lessema = lessema.substring(0, lessema.length() - 1);
                        return new Token("NUM", lessema);
                    }

                case 15:
                    carattere = readChar();
                    lessema += carattere;
                    if(carattere == '+' || carattere == '-') {
                        stato = 16;
                        break;
                    }
                    else
                        new Token("EOF");

                case 16:
                    carattere = readChar();
                    lessema += carattere;
                    if(!Character.isDigit(carattere)){
                        if(!isBlank(carattere))
                            retract();
                        lessema = lessema.substring(0, lessema.length() - 1);
                        return new Token("NUM", lessema);
                    }

            //id
                case 25:
                    carattere = readChar();
                    lessema += carattere;
                    if( !Character.isLetter(carattere)) {
                        if(carattere == '\uFFFF')  // controllo fine file
                            flag = false;
                        else
                            retract();             //retract solo se non si legge la fine della frase
                        lessema = lessema.substring(0, lessema.length() - 1);
                        Token t=installId(lessema);
                        resetLessema();
                        return t;
                    }
                    else if(isBlank(carattere)){
                        //retract();
                        lessema = lessema.substring(0, lessema.length() - 1);
                        Token t=installId(lessema);
                        resetLessema();
                        return t;
                    }
                    break;

            //commenti
                case 27:
                    carattere = readChar();
                    if(carattere == '\uFFFF')
                        new Token("EOF");
                    else if(carattere == '*')
                        stato = 28;
                    else if(carattere == '/')
                        stato = 30;
                    else
                        return installId(lessema);
                    break;

                case 28:
                    carattere = readChar();
                    if(carattere == '\uFFFF')
                        new Token("EOF");
                    if(carattere == '*')
                        stato = 29;
                    break;

                case 29:
                    carattere = readChar();
                    if(carattere == '\uFFFF' )
                        new Token("EOF");
                    if( carattere == '/'){
                        lessema="";
                        stato=0;
                    }

                    else
                        stato = 28;
                    break;

                case 30:
                    carattere = readChar();
                    if(carattere == '\uFFFF')
                        new Token("EOF");
                    if( carattere == '\n'){
                        lessema="";
                        stato=0;
                    }
                    else
                        stato = 30;
                    break;
            }
        }
        return null;
    }


    private char readChar() {
        char c = 0;
        try {
            reader.mark(0);
            c = (char) reader.read();
                return c;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return c;
    }



    private void resetLessema(){
        this.lessema="";
    }

    private static boolean isBlank(char carattere){
        return (carattere == ' ' || carattere =='\n' || carattere =='\t' || carattere =='\r');
    }
}
