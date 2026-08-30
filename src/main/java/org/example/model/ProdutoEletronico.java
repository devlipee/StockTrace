package org.example.model;

public class ProdutoEletronico extends Produto {
    private int mesesGarantia;

    public ProdutoEletronico(int id, double preco, String nome, int mesesGarantia) {
        super(id, preco, nome);
        this.mesesGarantia = mesesGarantia;
    }

    public int getMesesGarantia() {
        return mesesGarantia;
    }

    @Override
    public String toString() {
        return super.toString() +
                " | Meses de garantia: " + mesesGarantia;
    }
}
