package Java.Controle_de_estoque;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class controleEstoque {
    public static Scanner input;
    public static Produto[] listaProdutos = new Produto[100];
    public static int contador = 0;

    public static class Produto {
        String nome;
        int qtdEstoque;
        double precoUnitario;
        String categoria;
        int qtdMinima;
    }

    // --- 1. CADASTRAR ---
    public static void cadastrarProduto() {
        if (contador >= 100) {
            System.out.println("Erro: Estoque cheio!");
            return;
        }

        Produto produto = new Produto();
        System.out.println("\n--- Novo Cadastro ---");
        
        System.out.print("Nome do produto: ");
        produto.nome = input.nextLine();

        System.out.print("Qtd em estoque: ");
        produto.qtdEstoque = input.nextInt();

        System.out.print("Preço unitário: ");
        produto.precoUnitario = input.nextDouble();
        input.nextLine(); // Limpeza de Buffer Obrigatória

        System.out.print("Categoria: ");
        produto.categoria = input.nextLine();

        System.out.print("Qtd Mínima: ");
        produto.qtdMinima = input.nextInt();
        input.nextLine(); // Limpeza Final

        listaProdutos[contador] = produto;
        contador++;

        System.out.println("--> Produto salvo com sucesso!");
    }

    // --- 2. LISTAR ---
    public static void listarProdutos() {
        System.out.println("\n--- Listagem Geral ---");
        System.out.printf("%-20s | %-5s | %-10s | %-15s | %-5s\n", "NOME", "QTD", "PREÇO", "CATEGORIA", "MIN");
        System.out.println("--------------------------------------------------------------------");

        for (int i = 0; i < contador; i++) {
            Produto p = listaProdutos[i];
            System.out.printf("%-20s | %-5d | R$ %-7.2f | %-15s | %-5d\n",
                    p.nome, p.qtdEstoque, p.precoUnitario, p.categoria, p.qtdMinima);
        }
        System.out.println("--------------------------------------------------------------------");
    }

    // --- 3. FILTRAR POR CATEGORIA ---
    public static void filtrarPorCategoria() {
        System.out.print("\nDigite a categoria para filtrar: ");
        String busca = input.nextLine();
        
        System.out.println("\n--- Produtos da Categoria: " + busca + " ---");
        System.out.printf("%-20s | %-5s | %-10s\n", "NOME", "QTD", "PREÇO");
        
        boolean achou = false;
        for (int i = 0; i < contador; i++) {
            if (listaProdutos[i].categoria.equalsIgnoreCase(busca)) {
                System.out.printf("%-20s | %-5d | R$ %-7.2f\n", 
                    listaProdutos[i].nome, listaProdutos[i].qtdEstoque, listaProdutos[i].precoUnitario);
                achou = true;
            }
        }
        if (!achou) System.out.println("(Nenhum produto encontrado nesta categoria)");
    }

    // --- 4. ORDENAR (Por Nome) ---
    public static void ordenarProdutos() {
        // Algoritmo Bubble Sort
        for (int i = 0; i < contador - 1; i++) {
            for (int j = 0; j < contador - 1 - i; j++) {
                if (listaProdutos[j].nome.compareToIgnoreCase(listaProdutos[j + 1].nome) > 0) {
                    Produto temp = listaProdutos[j];
                    listaProdutos[j] = listaProdutos[j + 1];
                    listaProdutos[j + 1] = temp;
                }
            }
        }
        System.out.println("\n--> Estoque ordenado por Nome (A-Z)!");
        listarProdutos();
    }

    // Auxiliar: Ordenar por Categoria (Para a opção 7)
    public static void ordenarPorCategoria() {
        for (int i = 0; i < contador - 1; i++) {
            for (int j = 0; j < contador - 1 - i; j++) {
                if (listaProdutos[j].categoria.compareToIgnoreCase(listaProdutos[j + 1].categoria) > 0) {
                    Produto temp = listaProdutos[j];
                    listaProdutos[j] = listaProdutos[j + 1];
                    listaProdutos[j + 1] = temp;
                }
            }
        }
    }

    // --- 5. REMOVER ---
    public static void removerProduto() {
        System.out.print("\nDigite o nome do produto para remover: ");
        String nomeRemover = input.nextLine();
        int indice = -1;

        // 1. Buscar
        for (int i = 0; i < contador; i++) {
            if (listaProdutos[i].nome.equalsIgnoreCase(nomeRemover)) {
                indice = i;
                break;
            }
        }

        if (indice == -1) {
            System.out.println("Erro: Produto não encontrado.");
            return;
        }

        // 2. Remover (Shift Left)
        for (int i = indice; i < contador - 1; i++) {
            listaProdutos[i] = listaProdutos[i + 1];
        }
        listaProdutos[contador - 1] = null; // Limpa o último
        contador--; // Diminui o tamanho

        System.out.println("--> Produto removido com sucesso!");
    }

    // --- 6. ATUALIZAR PREÇO ---
    public static void atualizarPreco() {
        System.out.print("\nDigite o nome do produto para atualizar: ");
        String nomeBusca = input.nextLine();

        for (int i = 0; i < contador; i++) {
            if (listaProdutos[i].nome.equalsIgnoreCase(nomeBusca)) {
                System.out.println("Preço atual: R$ " + listaProdutos[i].precoUnitario);
                System.out.print("Novo preço: ");
                listaProdutos[i].precoUnitario = input.nextDouble();
                input.nextLine(); // Buffer
                System.out.println("--> Preço atualizado!");
                return;
            }
        }
        System.out.println("Erro: Produto não encontrado.");
    }

    // --- 7. RELATÓRIO COM SUBTOTAIS (O Desafio do Quadro) ---
    public static void relatorioFinanceiro() {
        ordenarPorCategoria(); // Agrupa as categorias
        
        System.out.println("\n--- Relatório Financeiro por Categoria ---");
        System.out.printf("%-15s | %-20s | %-10s\n", "CATEGORIA", "PRODUTO", "SUBTOTAL");
        System.out.println("--------------------------------------------------");

        double totalGeral = 0;
        String categoriaAtual = "";
        double subtotalCategoria = 0;

        for (int i = 0; i < contador; i++) {
            Produto p = listaProdutos[i];
            double valorTotalItem = p.qtdEstoque * p.precoUnitario;

            // Se mudou de categoria, imprime o subtotal da anterior (exceto na primeira volta)
            if (!p.categoria.equalsIgnoreCase(categoriaAtual)) {
                if (!categoriaAtual.equals("")) {
                    System.out.printf("   >>> Subtotal %s: R$ %.2f\n", categoriaAtual, subtotalCategoria);
                    System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - ");
                }
                categoriaAtual = p.categoria;
                subtotalCategoria = 0;
            }

            System.out.printf("%-15s | %-20s | R$ %-7.2f\n", "", p.nome, valorTotalItem);
            
            subtotalCategoria += valorTotalItem;
            totalGeral += valorTotalItem;
        }
        // Imprime o subtotal da última categoria
        if (!categoriaAtual.equals("")) {
            System.out.printf("   >>> Subtotal %s: R$ %.2f\n", categoriaAtual, subtotalCategoria);
        }

        System.out.println("==================================================");
        System.out.printf("TOTAL GERAL EM ESTOQUE: R$ %.2f\n", totalGeral);
        System.out.println("==================================================");
    }

    // --- MAIN (O Loop do Menu) ---
    public static void main(String[] args) throws FileNotFoundException {
        File arquivo = new File("digitacao.txt");
        input = new Scanner(arquivo);
        int opcao = 0;

        do {
            System.out.println("\n### SISTEMA DE ESTOQUE ###");
            System.out.println("1 - Cadastrar Produto");
            System.out.println("2 - Listar Tudo");
            System.out.println("3 - Filtrar por Categoria");
            System.out.println("4 - Ordenar por Nome");
            System.out.println("5 - Remover Produto");
            System.out.println("6 - Atualizar Preço");
            System.out.println("7 - Relatório Financeiro (Subtotais)");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            
            opcao = input.nextInt();
            input.nextLine(); // Limpeza de Buffer do menu

            switch (opcao) {
                case 1: cadastrarProduto(); break;
                case 2: listarProdutos(); break;
                case 3: filtrarPorCategoria(); break;
                case 4: ordenarProdutos(); break;
                case 5: removerProduto(); break;
                case 6: atualizarPreco(); break;
                case 7: relatorioFinanceiro(); break;
                case 0: System.out.println("Saindo..."); break;
                default: System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }
}   