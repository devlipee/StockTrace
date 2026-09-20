package org.stocktrace;

import org.stocktrace.model.CategoriaProduto;
import org.stocktrace.model.Estoque;
import org.stocktrace.model.Loja;
import org.stocktrace.model.Movimentacao;
import org.stocktrace.model.Produto;
import org.stocktrace.model.TipoMovimentacao;
import org.stocktrace.repository.EstoqueRepository;
import org.stocktrace.repository.LojaRepository;
import org.stocktrace.repository.MovimentacaoRepository;
import org.stocktrace.repository.ProdutoRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final LojaRepository lojaRepository =
            new LojaRepository();

    private static final ProdutoRepository produtoRepository =
            new ProdutoRepository();

    private static final EstoqueRepository estoqueRepository =
            new EstoqueRepository();

    private static final MovimentacaoRepository movimentacaoRepository =
            new MovimentacaoRepository();


    public static void main(String[] args) {

        int opcao;

        do {

            System.out.println("\n==============================");
            System.out.println("          STOCKTRACE");
            System.out.println("==============================");
            System.out.println("1 - Lojas");
            System.out.println("2 - Produtos");
            System.out.println("3 - Estoque e movimentações");
            System.out.println("0 - Sair");
            System.out.println("==============================");

            opcao = lerInt("Escolha uma opção: ");

            switch (opcao) {

                case 1 -> menuLojas();

                case 2 -> menuProdutos();

                case 3 -> menuEstoque();

                case 0 ->
                        System.out.println("Encerrando StockTrace...");

                default ->
                        System.out.println("Opção inválida.");
            }

        } while (opcao != 0);

        scanner.close();
    }


    // =========================================================
    // MENU LOJAS
    // =========================================================

    private static void menuLojas() {

        int opcao;

        do {

            System.out.println("\n========== LOJAS ==========");
            System.out.println("1 - Cadastrar loja");
            System.out.println("2 - Listar lojas");
            System.out.println("3 - Buscar loja por ID");
            System.out.println("4 - Atualizar loja");
            System.out.println("5 - Excluir loja");
            System.out.println("0 - Voltar");

            opcao = lerInt("Escolha uma opção: ");

            switch (opcao) {

                case 1 -> cadastrarLoja();

                case 2 -> listarLojas();

                case 3 -> buscarLoja();

                case 4 -> atualizarLoja();

                case 5 -> excluirLoja();

                case 0 -> {
                }

                default ->
                        System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }


    private static void cadastrarLoja() {

        try {

            System.out.println("\n--- CADASTRAR LOJA ---");

            String nome = lerTexto("Nome: ");
            String cidade = lerTexto("Cidade: ");
            String bairro = lerTexto("Bairro: ");
            String rua = lerTexto("Rua: ");
            String numero = lerTexto("Número: ");
            String complemento = lerTexto("Complemento: ");
            String cep = lerTexto("CEP: ");

            Loja loja = new Loja(
                    nome,
                    cidade,
                    bairro,
                    rua,
                    numero,
                    complemento,
                    cep
            );

            Loja lojaCadastrada =
                    lojaRepository.cadastrarLoja(loja);

            System.out.println("\nLoja cadastrada com sucesso!");

            mostrarLoja(lojaCadastrada);

        } catch (Exception e) {

            System.out.println(
                    "Erro ao cadastrar loja: " + e.getMessage()
            );
        }
    }


    private static void listarLojas() {

        try {

            List<Loja> lojas =
                    lojaRepository.listarLojas();

            System.out.println("\n--- LOJAS CADASTRADAS ---");

            if (lojas.isEmpty()) {

                System.out.println("Nenhuma loja cadastrada.");
                return;
            }

            for (Loja loja : lojas) {

                System.out.println("------------------------------");

                mostrarLoja(loja);
            }

        } catch (Exception e) {

            System.out.println(
                    "Erro ao listar lojas: " + e.getMessage()
            );
        }
    }


    private static void buscarLoja() {

        try {

            Long id =
                    lerLong("ID da loja: ");

            Loja loja =
                    lojaRepository.buscarLojaPorId(id);

            System.out.println("\n--- LOJA ENCONTRADA ---");

            mostrarLoja(loja);

        } catch (Exception e) {

            System.out.println(
                    "Erro ao buscar loja: " + e.getMessage()
            );
        }
    }


    private static void atualizarLoja() {

        try {

            listarLojasResumido();

            Long id =
                    lerLong("\nID da loja que deseja atualizar: ");

            Loja loja =
                    lojaRepository.buscarLojaPorId(id);

            System.out.println("\n--- NOVOS DADOS ---");

            String nome = lerTexto("Nome: ");
            String cidade = lerTexto("Cidade: ");
            String bairro = lerTexto("Bairro: ");
            String rua = lerTexto("Rua: ");
            String numero = lerTexto("Número: ");
            String complemento = lerTexto("Complemento: ");
            String cep = lerTexto("CEP: ");

            // Primeiro altera o objeto Loja.
            loja.atualizarDados(
                    nome,
                    cidade,
                    bairro,
                    rua,
                    numero,
                    complemento,
                    cep
            );

            // Depois atualiza o banco.
            lojaRepository.atualizarLoja(loja);

            System.out.println(
                    "\nLoja atualizada com sucesso!"
            );

        } catch (Exception e) {

            System.out.println(
                    "Erro ao atualizar loja: " + e.getMessage()
            );
        }
    }


    private static void excluirLoja() {

        try {

            listarLojasResumido();

            Long id =
                    lerLong("\nID da loja que deseja excluir: ");

            lojaRepository.deletarLoja(id);

            System.out.println(
                    "Loja excluída com sucesso!"
            );

        } catch (Exception e) {

            System.out.println(
                    "Erro ao excluir loja: " + e.getMessage()
            );
        }
    }


    // =========================================================
    // MENU PRODUTOS
    // =========================================================

    private static void menuProdutos() {

        int opcao;

        do {

            System.out.println("\n========== PRODUTOS ==========");
            System.out.println("1 - Cadastrar produto");
            System.out.println("2 - Listar produtos");
            System.out.println("3 - Buscar produto por ID");
            System.out.println("4 - Buscar produto por código");
            System.out.println("5 - Atualizar produto");
            System.out.println("6 - Ativar / Desativar produto");
            System.out.println("7 - Excluir produto");
            System.out.println("0 - Voltar");

            opcao = lerInt("Escolha uma opção: ");

            switch (opcao) {

                case 1 -> cadastrarProduto();

                case 2 -> listarProdutos();

                case 3 -> buscarProdutoPorId();

                case 4 -> buscarProdutoPorCodigo();

                case 5 -> atualizarProduto();

                case 6 -> alterarStatusProduto();

                case 7 -> excluirProduto();

                case 0 -> {
                }

                default ->
                        System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }


    private static void cadastrarProduto() {

        try {

            System.out.println("\n--- CADASTRAR PRODUTO ---");

            String codigo =
                    lerTexto("Código: ");

            String nome =
                    lerTexto("Nome: ");

            String descricao =
                    lerTexto("Descrição: ");

            BigDecimal preco =
                    lerBigDecimal("Preço: ");

            String unidadeMedida =
                    lerTexto("Unidade de medida: ");

            CategoriaProduto categoria =
                    escolherCategoria();

            Produto produto = new Produto(
                    codigo,
                    nome,
                    descricao,
                    preco,
                    unidadeMedida,
                    categoria
            );

            Produto produtoCadastrado =
                    produtoRepository.cadastrarProduto(produto);

            System.out.println(
                    "\nProduto cadastrado com sucesso!"
            );

            mostrarProduto(produtoCadastrado);

        } catch (Exception e) {

            System.out.println(
                    "Erro ao cadastrar produto: " + e.getMessage()
            );
        }
    }


    private static void listarProdutos() {

        try {

            List<Produto> produtos =
                    produtoRepository.listarProdutos();

            System.out.println("\n--- PRODUTOS CADASTRADOS ---");

            if (produtos.isEmpty()) {

                System.out.println(
                        "Nenhum produto cadastrado."
                );

                return;
            }

            for (Produto produto : produtos) {

                System.out.println("------------------------------");

                mostrarProduto(produto);
            }

        } catch (Exception e) {

            System.out.println(
                    "Erro ao listar produtos: " + e.getMessage()
            );
        }
    }


    private static void buscarProdutoPorId() {

        try {

            Long id =
                    lerLong("ID do produto: ");

            Produto produto =
                    produtoRepository.buscarProdutoPorId(id);

            System.out.println("\n--- PRODUTO ENCONTRADO ---");

            mostrarProduto(produto);

        } catch (Exception e) {

            System.out.println(
                    "Erro ao buscar produto: " + e.getMessage()
            );
        }
    }


    private static void buscarProdutoPorCodigo() {

        try {

            String codigo =
                    lerTexto("Código do produto: ");

            Produto produto =
                    produtoRepository.buscarProdutoPorCodigo(codigo);

            System.out.println("\n--- PRODUTO ENCONTRADO ---");

            mostrarProduto(produto);

        } catch (Exception e) {

            System.out.println(
                    "Erro ao buscar produto: " + e.getMessage()
            );
        }
    }


    private static void atualizarProduto() {

        try {

            listarProdutosResumido();

            Long id =
                    lerLong("\nID do produto que deseja atualizar: ");

            Produto produto =
                    produtoRepository.buscarProdutoPorId(id);

            System.out.println("\n--- NOVOS DADOS ---");

            String nome =
                    lerTexto("Nome: ");

            String descricao =
                    lerTexto("Descrição: ");

            BigDecimal preco =
                    lerBigDecimal("Preço: ");

            String unidadeMedida =
                    lerTexto("Unidade de medida: ");

            CategoriaProduto categoria =
                    escolherCategoria();

            // Altera os dados dentro do objeto.
            produto.atualizarDados(
                    nome,
                    descricao,
                    preco,
                    unidadeMedida
            );

            produto.alterarCategoria(categoria);

            // Salva os novos dados no banco.
            produtoRepository.atualizarProduto(produto);

            System.out.println(
                    "\nProduto atualizado com sucesso!"
            );

        } catch (Exception e) {

            System.out.println(
                    "Erro ao atualizar produto: " + e.getMessage()
            );
        }
    }


    private static void alterarStatusProduto() {

        try {

            listarProdutosResumido();

            Long id =
                    lerLong("\nID do produto: ");

            Produto produto =
                    produtoRepository.buscarProdutoPorId(id);

            if (produto.isAtivo()) {

                produto.desativar();

                produtoRepository.atualizarProduto(produto);

                System.out.println(
                        "Produto desativado com sucesso!"
                );

            } else {

                produto.ativar();

                produtoRepository.atualizarProduto(produto);

                System.out.println(
                        "Produto ativado com sucesso!"
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Erro ao alterar produto: " + e.getMessage()
            );
        }
    }


    private static void excluirProduto() {

        try {

            listarProdutosResumido();

            Long id =
                    lerLong("\nID do produto que deseja excluir: ");

            produtoRepository.deletarProduto(id);

            System.out.println(
                    "Produto excluído com sucesso!"
            );

        } catch (Exception e) {

            System.out.println(
                    "Erro ao excluir produto: " + e.getMessage()
            );
        }
    }


    // =========================================================
    // MENU ESTOQUE E MOVIMENTAÇÕES
    // =========================================================

    private static void menuEstoque() {

        int opcao;

        do {

            System.out.println(
                    "\n===== ESTOQUE E MOVIMENTAÇÕES ====="
            );

            System.out.println("1 - Registrar entrada");
            System.out.println("2 - Registrar saída");
            System.out.println("3 - Listar estoques");
            System.out.println("4 - Buscar estoque por ID");
            System.out.println("5 - Consultar produto em uma loja");
            System.out.println("6 - Listar movimentações");
            System.out.println("7 - Buscar movimentação por ID");
            System.out.println("8 - Histórico de um estoque");
            System.out.println("0 - Voltar");

            opcao = lerInt("Escolha uma opção: ");

            switch (opcao) {

                case 1 -> registrarEntrada();

                case 2 -> registrarSaida();

                case 3 -> listarEstoques();

                case 4 -> buscarEstoquePorId();

                case 5 -> consultarEstoqueProdutoLoja();

                case 6 -> listarMovimentacoes();

                case 7 -> buscarMovimentacao();

                case 8 -> historicoEstoque();

                case 0 -> {
                }

                default ->
                        System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }


    // =========================================================
    // ENTRADA DE ESTOQUE
    // =========================================================

    private static void registrarEntrada() {

        try {

            System.out.println("\n--- REGISTRAR ENTRADA ---");

            listarProdutosResumido();

            String codigo =
                    lerTexto("\nCódigo do produto: ");

            Produto produto =
                    produtoRepository.buscarProdutoPorCodigo(codigo);

            listarLojasResumido();

            Long lojaId =
                    lerLong("\nID da loja: ");

            Loja loja =
                    lojaRepository.buscarLojaPorId(lojaId);

            int quantidade =
                    lerInt("Quantidade de entrada: ");

            String responsavel =
                    lerTexto("Responsável: ");

            String motivo =
                    lerTexto("Motivo: ");

            String observacao =
                    lerTexto("Observação: ");

            /*
             * Verifica se já existe um estoque
             * deste produto nesta loja.
             */
            Estoque estoque =
                    estoqueRepository.buscarEstoquePorProdutoELoja(
                            produto.getId(),
                            loja.getId()
                    );

            /*
             * Se ainda não existir estoque,
             * cria um novo com quantidade zero.
             */
            if (estoque == null) {

                estoque =
                        new Estoque(produto, loja);

                estoque =
                        estoqueRepository.cadastrarEstoque(
                                estoque
                        );
            }

            /*
             * Aumenta a quantidade dentro
             * do objeto Estoque.
             */
            estoque.adicionar(quantidade);

            /*
             * Salva a nova quantidade no banco.
             */
            estoqueRepository.atualizarQuantidade(
                    estoque
            );

            /*
             * Registra o histórico da operação.
             */
            Movimentacao movimentacao =
                    new Movimentacao(
                            estoque,
                            TipoMovimentacao.ENTRADA,
                            quantidade,
                            motivo,
                            observacao,
                            responsavel
                    );

            Movimentacao movimentacaoCadastrada =
                    movimentacaoRepository.cadastrarMovimentacao(
                            movimentacao
                    );

            System.out.println(
                    "\nEntrada registrada com sucesso!"
            );

            System.out.println(
                    "Movimentação ID: "
                            + movimentacaoCadastrada.getId()
            );

            System.out.println(
                    "Produto: "
                            + produto.getNome()
            );

            System.out.println(
                    "Loja: "
                            + loja.getNome()
            );

            System.out.println(
                    "Quantidade atual: "
                            + estoque.getQuantidadeAtual()
            );

        } catch (Exception e) {

            System.out.println(
                    "Erro ao registrar entrada: "
                            + e.getMessage()
            );
        }
    }


    // =========================================================
    // SAÍDA DE ESTOQUE
    // =========================================================

    private static void registrarSaida() {

        try {

            System.out.println("\n--- REGISTRAR SAÍDA ---");

            listarProdutosResumido();

            String codigo =
                    lerTexto("\nCódigo do produto: ");

            Produto produto =
                    produtoRepository.buscarProdutoPorCodigo(codigo);

            listarLojasResumido();

            Long lojaId =
                    lerLong("\nID da loja: ");

            Loja loja =
                    lojaRepository.buscarLojaPorId(lojaId);

            /*
             * Procura o estoque do produto
             * na loja escolhida.
             */
            Estoque estoque =
                    estoqueRepository.buscarEstoquePorProdutoELoja(
                            produto.getId(),
                            loja.getId()
                    );

            if (estoque == null) {

                System.out.println(
                        "Esse produto não possui estoque nessa loja."
                );

                return;
            }

            System.out.println(
                    "Quantidade disponível: "
                            + estoque.getQuantidadeAtual()
            );

            int quantidade =
                    lerInt("Quantidade de saída: ");

            String responsavel =
                    lerTexto("Responsável: ");

            String motivo =
                    lerTexto("Motivo: ");

            String observacao =
                    lerTexto("Observação: ");

            /*
             * O método retirar() verifica:
             *
             * - quantidade maior que zero
             * - estoque suficiente
             */
            estoque.retirar(quantidade);

            estoqueRepository.atualizarQuantidade(
                    estoque
            );

            Movimentacao movimentacao =
                    new Movimentacao(
                            estoque,
                            TipoMovimentacao.SAIDA,
                            quantidade,
                            motivo,
                            observacao,
                            responsavel
                    );

            Movimentacao movimentacaoCadastrada =
                    movimentacaoRepository.cadastrarMovimentacao(
                            movimentacao
                    );

            System.out.println(
                    "\nSaída registrada com sucesso!"
            );

            System.out.println(
                    "Movimentação ID: "
                            + movimentacaoCadastrada.getId()
            );

            System.out.println(
                    "Quantidade restante: "
                            + estoque.getQuantidadeAtual()
            );

        } catch (Exception e) {

            System.out.println(
                    "Erro ao registrar saída: "
                            + e.getMessage()
            );
        }
    }


    // =========================================================
    // CONSULTAS DE ESTOQUE
    // =========================================================

    private static void listarEstoques() {

        try {

            List<Estoque> estoques =
                    estoqueRepository.listarEstoques();

            System.out.println("\n--- ESTOQUES ---");

            if (estoques.isEmpty()) {

                System.out.println(
                        "Nenhum estoque cadastrado."
                );

                return;
            }

            for (Estoque estoque : estoques) {

                System.out.println("------------------------------");

                mostrarEstoque(estoque);
            }

        } catch (Exception e) {

            System.out.println(
                    "Erro ao listar estoques: "
                            + e.getMessage()
            );
        }
    }


    private static void buscarEstoquePorId() {

        try {

            Long id =
                    lerLong("ID do estoque: ");

            Estoque estoque =
                    estoqueRepository.buscarEstoquePorId(id);

            System.out.println("\n--- ESTOQUE ENCONTRADO ---");

            mostrarEstoque(estoque);

        } catch (Exception e) {

            System.out.println(
                    "Erro ao buscar estoque: "
                            + e.getMessage()
            );
        }
    }


    private static void consultarEstoqueProdutoLoja() {

        try {

            listarProdutosResumido();

            Long produtoId =
                    lerLong("\nID do produto: ");

            listarLojasResumido();

            Long lojaId =
                    lerLong("\nID da loja: ");

            Estoque estoque =
                    estoqueRepository.buscarEstoquePorProdutoELoja(
                            produtoId,
                            lojaId
                    );

            if (estoque == null) {

                System.out.println(
                        "Não existe estoque desse produto nessa loja."
                );

                return;
            }

            System.out.println("\n--- ESTOQUE ENCONTRADO ---");

            mostrarEstoque(estoque);

        } catch (Exception e) {

            System.out.println(
                    "Erro: " + e.getMessage()
            );
        }
    }


    // =========================================================
    // CONSULTAS DE MOVIMENTAÇÃO
    // =========================================================

    private static void listarMovimentacoes() {

        try {

            List<Movimentacao> movimentacoes =
                    movimentacaoRepository.listarMovimentacoes();

            System.out.println(
                    "\n--- MOVIMENTAÇÕES ---"
            );

            if (movimentacoes.isEmpty()) {

                System.out.println(
                        "Nenhuma movimentação registrada."
                );

                return;
            }

            for (Movimentacao movimentacao : movimentacoes) {

                System.out.println("------------------------------");

                mostrarMovimentacao(movimentacao);
            }

        } catch (Exception e) {

            System.out.println(
                    "Erro ao listar movimentações: "
                            + e.getMessage()
            );
        }
    }


    private static void buscarMovimentacao() {

        try {

            Long id =
                    lerLong("ID da movimentação: ");

            Movimentacao movimentacao =
                    movimentacaoRepository.buscarMovimentacaoPorId(id);

            System.out.println(
                    "\n--- MOVIMENTAÇÃO ENCONTRADA ---"
            );

            mostrarMovimentacao(movimentacao);

        } catch (Exception e) {

            System.out.println(
                    "Erro: " + e.getMessage()
            );
        }
    }


    private static void historicoEstoque() {

        try {

            listarEstoquesResumido();

            Long estoqueId =
                    lerLong("\nID do estoque: ");

            List<Movimentacao> movimentacoes =
                    movimentacaoRepository.listarMovimentacoesPorEstoque(
                            estoqueId
                    );

            System.out.println(
                    "\n--- HISTÓRICO DO ESTOQUE ---"
            );

            if (movimentacoes.isEmpty()) {

                System.out.println(
                        "Nenhuma movimentação encontrada."
                );

                return;
            }

            for (Movimentacao movimentacao : movimentacoes) {

                System.out.println("------------------------------");

                mostrarMovimentacao(movimentacao);
            }

        } catch (Exception e) {

            System.out.println(
                    "Erro: " + e.getMessage()
            );
        }
    }


    // =========================================================
    // ESCOLHA DE CATEGORIA
    // =========================================================

    private static CategoriaProduto escolherCategoria() {

        CategoriaProduto[] categorias =
                CategoriaProduto.values();

        System.out.println("\n--- CATEGORIAS ---");

        for (int i = 0; i < categorias.length; i++) {

            System.out.println(
                    (i + 1)
                            + " - "
                            + categorias[i]
                            .name()
                            .replace("_", " ")
            );
        }

        while (true) {

            int opcao =
                    lerInt("Escolha a categoria: ");

            if (
                    opcao >= 1
                            && opcao <= categorias.length
            ) {

                return categorias[opcao - 1];
            }

            System.out.println(
                    "Categoria inválida."
            );
        }
    }


    // =========================================================
    // EXIBIÇÃO DOS OBJETOS
    // =========================================================

    private static void mostrarLoja(Loja loja) {

        System.out.println(
                "ID: " + loja.getId()
        );

        System.out.println(
                "Nome: " + loja.getNome()
        );

        System.out.println(
                "Endereço: "
                        + loja.getEnderecoCompleto()
        );

        System.out.println(
                "CEP: " + loja.getCep()
        );
    }


    private static void mostrarProduto(Produto produto) {

        System.out.println(
                "ID: " + produto.getId()
        );

        System.out.println(
                "Código: " + produto.getCodigo()
        );

        System.out.println(
                "Nome: " + produto.getNome()
        );

        System.out.println(
                "Descrição: " + produto.getDescricao()
        );

        System.out.println(
                "Preço: R$ " + produto.getPreco()
        );

        System.out.println(
                "Unidade: "
                        + produto.getUnidadeMedida()
        );

        System.out.println(
                "Categoria: "
                        + produto.getCategoria()
        );

        System.out.println(
                "Ativo: "
                        + produto.isAtivo()
        );
    }


    private static void mostrarEstoque(Estoque estoque) {

        System.out.println(
                "ID do estoque: "
                        + estoque.getId()
        );

        System.out.println(
                "Produto: "
                        + estoque.getProduto().getNome()
        );

        System.out.println(
                "Loja: "
                        + estoque.getLoja().getNome()
        );

        System.out.println(
                "Quantidade atual: "
                        + estoque.getQuantidadeAtual()
        );
    }


    private static void mostrarMovimentacao(
            Movimentacao movimentacao
    ) {

        System.out.println(
                "ID: "
                        + movimentacao.getId()
        );

        System.out.println(
                "Tipo: "
                        + movimentacao.getTipo()
        );

        System.out.println(
                "Produto: "
                        + movimentacao
                        .getEstoque()
                        .getProduto()
                        .getNome()
        );

        System.out.println(
                "Loja: "
                        + movimentacao
                        .getEstoque()
                        .getLoja()
                        .getNome()
        );

        System.out.println(
                "Quantidade: "
                        + movimentacao.getQuantidade()
        );

        System.out.println(
                "Data/Hora: "
                        + movimentacao.getDataHora()
        );

        System.out.println(
                "Responsável: "
                        + movimentacao.getResponsavel()
        );

        System.out.println(
                "Motivo: "
                        + movimentacao.getMotivo()
        );

        System.out.println(
                "Observação: "
                        + movimentacao.getObservacao()
        );
    }


    // =========================================================
    // LISTAGENS RESUMIDAS
    // =========================================================

    private static void listarLojasResumido()
            throws Exception {

        System.out.println("\n--- LOJAS ---");

        for (Loja loja :
                lojaRepository.listarLojas()) {

            System.out.println(
                    loja.getId()
                            + " - "
                            + loja.getNome()
            );
        }
    }


    private static void listarProdutosResumido()
            throws Exception {

        System.out.println("\n--- PRODUTOS ---");

        for (Produto produto :
                produtoRepository.listarProdutos()) {

            System.out.println(
                    produto.getId()
                            + " - "
                            + produto.getCodigo()
                            + " - "
                            + produto.getNome()
            );
        }
    }


    private static void listarEstoquesResumido()
            throws Exception {

        System.out.println("\n--- ESTOQUES ---");

        for (Estoque estoque :
                estoqueRepository.listarEstoques()) {

            System.out.println(
                    estoque.getId()
                            + " - "
                            + estoque.getProduto().getNome()
                            + " - "
                            + estoque.getLoja().getNome()
                            + " - Quantidade: "
                            + estoque.getQuantidadeAtual()
            );
        }
    }


    // =========================================================
    // LEITURA DOS DADOS DO CONSOLE
    // =========================================================

    private static String lerTexto(String mensagem) {

        System.out.print(mensagem);

        return scanner.nextLine().trim();
    }


    private static int lerInt(String mensagem) {

        while (true) {

            try {

                return Integer.parseInt(
                        lerTexto(mensagem)
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Digite um número inteiro válido."
                );
            }
        }
    }


    private static Long lerLong(String mensagem) {

        while (true) {

            try {

                return Long.parseLong(
                        lerTexto(mensagem)
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Digite um ID válido."
                );
            }
        }
    }


    private static BigDecimal lerBigDecimal(
            String mensagem
    ) {

        while (true) {

            try {

                String valor =
                        lerTexto(mensagem)
                                .replace(",", ".");

                return new BigDecimal(valor);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Digite um valor válido."
                );
            }
        }
    }
}