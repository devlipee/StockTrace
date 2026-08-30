package org.example;

public class ProdutoEletronico extends Produto{
    private int mesesGarantia;

    public ProdutoEletronico(int id, double preco, String nome, int mesesGarantia) {
        super(id, preco, nome);
        this.mesesGarantia = mesesGarantia;
    }

    public int getMesesGarantia() {
        return mesesGarantia;
    }
}
