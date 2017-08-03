package functionaljasper;

public class CodigoNome {

    private String codigo; // Código do retorno
    private String nome;   // Descrição do retonrno

    public CodigoNome() {

    }

    public CodigoNome(String codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}