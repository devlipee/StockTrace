package org.stocktrace;

import org.stocktrace.exception.StockTraceException;
import org.stocktrace.model.CategoriaProduto;
import org.stocktrace.model.Estoque;
import org.stocktrace.model.Loja;
import org.stocktrace.model.Movimentacao;
import org.stocktrace.model.Produto;
import org.stocktrace.service.EstoqueService;
import org.stocktrace.service.LojaService;
import org.stocktrace.service.MovimentacaoService;
import org.stocktrace.service.ProdutoService;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final LojaService lojaService = new LojaService();
    private static final ProdutoService produtoService = new ProdutoService();
    private static final EstoqueService estoqueService = new EstoqueService();

    private static final MovimentacaoService movimentacaoService =
            new MovimentacaoService();

    private static final DateTimeFormatter formatoData =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static void main(String[] args) {
        try (scanner) {
            while (true) {
                System.out.println("""
                        
                        ===== STOCKTRACE =====
                        1 - Lojas
                        2 - Produtos
                        3 - Estoques
                        4 - Movimentações
                        0 - Sair
                        """);

                try {
                    switch (lerTexto("Escolha: ")) {
                        case "1" -> menuLojas();
                        case "2" -> menuProdutos();
                        case "3" -> menuEstoques();
                        case "4" -> menuMovimentacoes();

                        case "0" -> {
                            System.out.println("Encerrando StockTrace...");
                            return;
                        }

                        default -> System.out.println("Opção inválida.");
                    }

                } catch (StockTraceException erro) {
                    System.out.println(
                            "Não foi possível concluir: " + erro.getMessage()
                    );
                    System.out.println("Voltando ao menu principal.");

                } catch (SQLException erro) {
                    System.out.println(
                            "Não foi possível concluir a operação no banco."
                    );
                    System.out.println(
                            "Código SQL: " + erro.getErrorCode()
                                    + " | Estado SQL: " + erro.getSQLState()
                    );
                    System.out.println(
                            "Confira os dados antes de repetir uma gravação."
                    );

                } catch (IOException erro) {
                    System.out.println(
                            "Não foi possível ler a configuração do banco."
                    );
                    System.out.println(
                            "Confira o arquivo config.properties."
                    );
                }
            }

        } catch (NoSuchElementException erro) {
            System.out.println(
                    "\nEntrada de dados encerrada. Finalizando StockTrace."
            );
        }
    }

    // ==================== LOJAS ====================

    private static void menuLojas() throws SQLException, IOException {
        while (true) {
            System.out.println("""
                    
                    ===== LOJAS =====
                    1 - Cadastrar
                    2 - Listar
                    3 - Buscar por ID
                    4 - Atualizar
                    5 - Excluir
                    0 - Voltar
                    """);

            switch (lerTexto("Escolha: ")) {
                case "1" -> salvarLoja(false);

                case "2" -> {
                    List<Loja> lojas = lojaService.listarLojas();

                    if (lojas.isEmpty()) {
                        System.out.println("Nenhuma loja cadastrada.");
                    }

                    for (Loja loja : lojas) {
                        mostrarLoja(loja);
                    }
                }

                case "3" -> mostrarLoja(
                        lojaService.buscarLojaPorId(lerId("ID da loja: "))
                );

                case "4" -> salvarLoja(true);

                case "5" -> {
                    Long id = lerId("ID da loja: ");

                    mostrarLoja(lojaService.buscarLojaPorId(id));

                    if (confirmar("Excluir esta loja?")) {
                        lojaService.deletarLoja(id);
                        System.out.println("Loja excluída.");
                    }
                }

                case "0" -> {
                    return;
                }

                default -> System.out.println("Opção inválida.");
            }
        }
    }

    // Reaproveita a leitura dos campos no cadastro e na atualização.
    private static void salvarLoja(boolean atualizar)
            throws SQLException, IOException {

        Long id = null;

        if (atualizar) {
            id = lerId("ID da loja: ");

            mostrarLoja(lojaService.buscarLojaPorId(id));

            System.out.println("Informe todos os novos dados da loja.");
        }

        String nome = lerTexto("Nome: ");
        String cidade = lerTexto("Cidade: ");
        String bairro = lerTexto("Bairro: ");
        String rua = lerTexto("Rua: ");
        String numero = lerTexto("Número: ");
        String complemento = lerOpcional("Complemento (opcional): ");
        String cep = lerTexto("CEP: ");

        Loja loja;

        if (atualizar) {
            loja = lojaService.atualizarLoja(
                    id,
                    nome,
                    cidade,
                    bairro,
                    rua,
                    numero,
                    complemento,
                    cep
            );
        } else {
            loja = lojaService.cadastrarLoja(
                    nome,
                    cidade,
                    bairro,
                    rua,
                    numero,
                    complemento,
                    cep
            );
        }

        System.out.println("Loja salva.");
        mostrarLoja(loja);
    }

    // ==================== PRODUTOS ====================

    private static void menuProdutos() throws SQLException, IOException {
        while (true) {
            System.out.println("""
                    
                    ===== PRODUTOS =====
                    1 - Cadastrar
                    2 - Listar
                    3 - Buscar por ID
                    4 - Buscar por código
                    5 - Atualizar dados
                    6 - Alterar categoria
                    7 - Ativar
                    8 - Desativar
                    9 - Excluir
                    0 - Voltar
                    """);

            switch (lerTexto("Escolha: ")) {
                case "1" -> salvarProduto(false);

                case "2" -> {
                    List<Produto> produtos = produtoService.listaProdutos();

                    if (produtos.isEmpty()) {
                        System.out.println("Nenhum produto cadastrado.");
                    }

                    for (Produto produto : produtos) {
                        mostrarProduto(produto);
                    }
                }

                case "3" -> mostrarProduto(
                        produtoService.buscarProdutoPorId(
                                lerId("ID do produto: ")
                        )
                );

                case "4" -> mostrarProduto(
                        produtoService.buscarProdutoPorCodigo(
                                lerTexto("Código: ")
                        )
                );

                case "5" -> salvarProduto(true);

                case "6" -> {
                    Long id = lerId("ID do produto: ");

                    mostrarProduto(produtoService.buscarProdutoPorId(id));

                    CategoriaProduto categoria = lerCategoria();

                    mostrarProduto(
                            produtoService.alterarCategoriaProduto(
                                    id,
                                    categoria
                            )
                    );

                    System.out.println("Categoria alterada.");
                }

                case "7" -> {
                    mostrarProduto(
                            produtoService.ativarProduto(
                                    lerId("ID do produto: ")
                            )
                    );

                    System.out.println("Produto ativado.");
                }

                case "8" -> {
                    mostrarProduto(
                            produtoService.desativarProduto(
                                    lerId("ID do produto: ")
                            )
                    );

                    System.out.println("Produto desativado.");
                }

                case "9" -> {
                    Long id = lerId("ID do produto: ");

                    mostrarProduto(produtoService.buscarProdutoPorId(id));

                    if (confirmar("Excluir este produto?")) {
                        produtoService.deletarProduto(id);
                        System.out.println("Produto excluído.");
                    }
                }

                case "0" -> {
                    return;
                }

                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void salvarProduto(boolean atualizar)
            throws SQLException, IOException {

        Long id = null;
        String codigo = null;

        if (atualizar) {
            id = lerId("ID do produto: ");

            mostrarProduto(produtoService.buscarProdutoPorId(id));

            System.out.println(
                    "Informe nome, descrição, preço e unidade novos."
            );
        } else {
            codigo = lerTexto("Código: ");
        }

        String nome = lerTexto("Nome: ");
        String descricao = lerTexto("Descrição: ");
        BigDecimal preco = lerPreco();
        String unidade = lerTexto("Unidade de medida (ex.: UN, KG): ");

        Produto produto;

        if (atualizar) {
            produto = produtoService.atualizarProduto(
                    id,
                    nome,
                    descricao,
                    preco,
                    unidade
            );
        } else {
            CategoriaProduto categoria = lerCategoria();

            produto = produtoService.cadastrarProduto(
                    codigo,
                    nome,
                    descricao,
                    preco,
                    unidade,
                    categoria
            );
        }

        System.out.println("Produto salvo.");
        mostrarProduto(produto);
    }

    // ==================== ESTOQUES ====================

    private static void menuEstoques() throws SQLException, IOException {
        while (true) {
            System.out.println("""
                    
                    ===== ESTOQUES =====
                    1 - Cadastrar estoque de um produto em uma loja
                    2 - Listar
                    3 - Buscar por ID
                    4 - Buscar por produto e loja
                    0 - Voltar
                    """);

            switch (lerTexto("Escolha: ")) {
                case "1" -> {
                    Long produtoId = lerId("ID do produto: ");
                    Long lojaId = lerId("ID da loja: ");

                    Estoque estoque = estoqueService.cadastrarEstoque(
                            produtoId,
                            lojaId
                    );

                    System.out.println("Estoque cadastrado com saldo zero.");
                    mostrarEstoque(estoque);
                }

                case "2" -> {
                    List<Estoque> estoques = estoqueService.listarEstoques();

                    if (estoques.isEmpty()) {
                        System.out.println("Nenhum estoque cadastrado.");
                    }

                    for (Estoque estoque : estoques) {
                        mostrarEstoque(estoque);
                    }
                }

                case "3" -> mostrarEstoque(
                        estoqueService.buscarEstoquePorId(
                                lerId("ID do estoque: ")
                        )
                );

                case "4" -> {
                    Long produtoId = lerId("ID do produto: ");
                    Long lojaId = lerId("ID da loja: ");

                    Estoque estoque =
                            estoqueService.buscarEstoquePorProdutoELoja(
                                    produtoId,
                                    lojaId
                            );

                    if (estoque == null) {
                        System.out.println(
                                "Ainda não existe estoque desse produto nessa loja."
                        );
                    } else {
                        mostrarEstoque(estoque);
                    }
                }

                case "0" -> {
                    return;
                }

                default -> System.out.println("Opção inválida.");
            }
        }
    }

    // ==================== MOVIMENTAÇÕES ====================

    private static void menuMovimentacoes()
            throws SQLException, IOException {

        while (true) {
            System.out.println("""
                    
                    ===== MOVIMENTAÇÕES =====
                    1 - Registrar entrada
                    2 - Registrar saída
                    3 - Listar todas
                    4 - Buscar por ID
                    5 - Histórico de um estoque
                    0 - Voltar
                    """);

            switch (lerTexto("Escolha: ")) {
                case "1" -> registrarMovimentacao(true);

                case "2" -> registrarMovimentacao(false);

                case "3" -> mostrarMovimentacoes(
                        movimentacaoService.listarMovimentacoes()
                );

                case "4" -> mostrarMovimentacao(
                        movimentacaoService.buscarMovimentacaoPorId(
                                lerId("ID da movimentação: ")
                        )
                );

                case "5" -> mostrarMovimentacoes(
                        movimentacaoService.listarMovimentacoesPorEstoque(
                                lerId("ID do estoque: ")
                        )
                );

                case "0" -> {
                    return;
                }

                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void registrarMovimentacao(boolean entrada)
            throws SQLException, IOException {

        Long estoqueId = lerId("ID do estoque: ");

        // Exibe os dados para o usuário.
        // O service busca e bloqueia o saldo novamente na transação.
        mostrarEstoque(estoqueService.buscarEstoquePorId(estoqueId));

        int quantidade = lerInteiro("Quantidade: ");
        String motivo = lerTexto("Motivo: ");
        String observacao = lerOpcional("Observação (opcional): ");
        String responsavel = lerTexto("Responsável: ");

        Movimentacao movimentacao;

        if (entrada) {
            movimentacao = movimentacaoService.registrarEntrada(
                    estoqueId,
                    quantidade,
                    motivo,
                    observacao,
                    responsavel
            );
        } else {
            movimentacao = movimentacaoService.registrarSaida(
                    estoqueId,
                    quantidade,
                    motivo,
                    observacao,
                    responsavel
            );
        }

        System.out.println("Movimentação registrada.");
        mostrarMovimentacao(movimentacao);

        System.out.println(
                "Saldo após esta movimentação: "
                        + movimentacao.getEstoque().getQuantidadeAtual()
        );
    }

    // ==================== EXIBIÇÃO ====================

    private static void mostrarLoja(Loja loja) {
        System.out.println(
                "\nID: " + loja.getId()
                        + " | Loja: " + loja.getNome()
        );

        System.out.println("Endereço: " + loja.getEnderecoCompleto());

        System.out.println(
                "Complemento: " + exibirOpcional(loja.getComplemento())
                        + " | CEP: " + exibirOpcional(loja.getCep())
        );
    }

    private static void mostrarProduto(Produto produto) {
        System.out.println(
                "\nID: " + produto.getId()
                        + " | Código: " + produto.getCodigo()
                        + " | Produto: " + produto.getNome()
        );

        System.out.println("Descrição: " + produto.getDescricao());

        System.out.println(
                "Preço: " + produto.getPreco().toPlainString()
                        + " | Unidade: " + produto.getUnidadeMedida()
        );

        System.out.println(
                "Categoria: " + produto.getCategoria()
                        + " | Situação: "
                        + (produto.isAtivo() ? "Ativo" : "Inativo")
        );
    }

    private static void mostrarEstoque(Estoque estoque) {
        System.out.println("\nEstoque ID: " + estoque.getId());

        System.out.println(
                "Produto: " + estoque.getProduto().getNome()
                        + " (ID " + estoque.getProduto().getId() + ")"
        );

        System.out.println(
                "Loja: " + estoque.getLoja().getNome()
                        + " (ID " + estoque.getLoja().getId() + ")"
        );

        System.out.println("Saldo: " + estoque.getQuantidadeAtual());
    }

    private static void mostrarMovimentacoes(
            List<Movimentacao> movimentacoes
    ) {
        if (movimentacoes.isEmpty()) {
            System.out.println("Nenhuma movimentação encontrada.");
        }

        for (Movimentacao movimentacao : movimentacoes) {
            mostrarMovimentacao(movimentacao);
        }
    }

    private static void mostrarMovimentacao(Movimentacao movimentacao) {
        System.out.println(
                "\nMovimentação ID: " + movimentacao.getId()
                        + " | Estoque ID: "
                        + movimentacao.getEstoque().getId()
        );

        System.out.println(movimentacao.getDescricao());

        System.out.println(
                "Data: " + movimentacao.getDataHora().format(formatoData)
        );

        System.out.println("Motivo: " + movimentacao.getMotivo());

        System.out.println(
                "Observação: "
                        + exibirOpcional(movimentacao.getObservacao())
        );
    }

    // ==================== LEITURA DE DADOS ====================

    // Usamos nextLine em todas as leituras para evitar
    // quebras de linha pendentes no Scanner.
    private static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    private static String lerOpcional(String mensagem) {
        String valor = lerTexto(mensagem);

        return valor.isBlank() ? null : valor;
    }

    private static String exibirOpcional(String valor) {
        return valor == null || valor.isBlank()
                ? "Não informado"
                : valor;
    }

    private static Long lerId(String mensagem) {
        while (true) {
            try {
                return Long.parseLong(lerTexto(mensagem));

            } catch (NumberFormatException erro) {
                System.out.println(
                        "Digite um número inteiro válido para o ID."
                );
            }
        }
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            try {
                return Integer.parseInt(lerTexto(mensagem));

            } catch (NumberFormatException erro) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    private static BigDecimal lerPreco() {
        while (true) {
            String valor = lerTexto(
                    "Preço (ex.: 25,90; sem separador de milhar): "
            );

            if (valor.matches("[+-]?[0-9]+([.,][0-9]+)?")) {
                return new BigDecimal(valor.replace(',', '.'));
            }

            System.out.println(
                    "Preço inválido. Use um número como 25,90 ou 25.90."
            );
        }
    }

    private static CategoriaProduto lerCategoria() {
        CategoriaProduto[] categorias = CategoriaProduto.values();

        while (true) {
            System.out.println("Categorias:");

            for (int i = 0; i < categorias.length; i++) {
                System.out.println((i + 1) + " - " + categorias[i]);
            }

            int opcao = lerInteiro("Categoria: ");

            if (opcao >= 1 && opcao <= categorias.length) {
                return categorias[opcao - 1];
            }

            System.out.println("Escolha uma das categorias da lista.");
        }
    }

    private static boolean confirmar(String mensagem) {
        while (true) {
            String resposta = lerTexto(mensagem + " [s/n]: ");

            if (resposta.equalsIgnoreCase("s")) {
                return true;
            }

            if (resposta.equalsIgnoreCase("n")) {
                System.out.println("Operação cancelada.");
                return false;
            }

            System.out.println(
                    "Digite s para confirmar ou n para cancelar."
            );
        }
    }
}