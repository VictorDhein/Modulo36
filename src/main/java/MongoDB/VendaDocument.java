import org.bson.Document;

import java.util.List;

public class VendaDocument {

    private int anoDeFabricacao;
    private String modelo;
    private String marca;
    private double valorDeVenda;
    private double comissao;
    private List<String> modelosVendidos;

    // Getters e Setters
    public int getAnoDeFabricacao() {
        return anoDeFabricacao;
    }

    public void setAnoDeFabricacao(int anoDeFabricacao) {
        this.anoDeFabricacao = anoDeFabricacao;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getValorDeVenda() {
        return valorDeVenda;
    }

    public void setValorDeVenda(double valorDeVenda) {
        this.valorDeVenda = valorDeVenda;
    }

    public double getComissao() {
        return comissao;
    }

    public void setComissao(double comissao) {
        this.comissao = comissao;
    }

    public List<String> getModelosVendidos() {
        return modelosVendidos;
    }

    public void setModelosVendidos(List<String> modelosVendidos) {
        this.modelosVendidos = modelosVendidos;
    }


    public Documentos toDocumentos() {
        return new Documentos()
                .append("anoDeFabricacao", this.anoDeFabricacao)
                .append("modelo", this.modelo)
                .append("marca", this.marca)
                .append("valorDeVenda", this.valorDeVenda)
                .append("comissao", this.comissao)
                .append("modelosVendidos", this.modelosVendidos);
    }


    @org.jetbrains.annotations.NotNull
    public static VendaDocument fromDocument(Documentos documentos) {
        VendaDocument venda = new VendaDocument();
        venda.setAnoDeFabricacao(documentos.getInteger("anoDeFabricacao"));
        venda.setModelo(documentos.getString("modelo"));
        venda.setMarca(documentos.getString("marca"));
        venda.setValorDeVenda(documentos.getDouble("valorDeVenda"));
        venda.setComissao(documentos.getDouble("comissao"));
        venda.setModelosVendidos((List<String>) documentos.get("modelosVendidos"));
        return venda;
    }
}
