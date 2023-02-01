public class Token {

    private String tokenId;
    private String attributo;

    public Token(String tokenId) {
        this.tokenId = tokenId;
        this.attributo = null;
    }

    public Token(String tokenId, String attributo) {
        this.tokenId = tokenId;
        this.attributo = attributo;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String nome) {
        this.tokenId = nome;
    }

    public String getAttributo() {
        return attributo;
    }

    public void setAttributo(String attributo) {
        this.attributo = attributo;
    }

    @Override
    public String toString() {
        return "Lexer{" +
                "nome='" + tokenId + '\'' +
                ", attributo='" + attributo + '\'' +
                '}';
    }
}
