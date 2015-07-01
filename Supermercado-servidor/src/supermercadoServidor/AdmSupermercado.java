package supermercadoServidor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/*
 * Classe utilizada para administrar o sistema interno,
 * adicionar produtos, atualizar estoque, gerar relatorios.
*/
public class AdmSupermercado implements Runnable {
	
	private CSVReader lerP() throws FileNotFoundException {
		CSVReader lerP = new CSVReader(new FileReader("arquivos/produtos.csv"));
		return lerP;
	}
	
	private String entradaS() {
		@SuppressWarnings("resource")
		Scanner ler = new Scanner(System.in);
		return ler.nextLine();
	}
	
	private int entradaI() {
		@SuppressWarnings("resource")
		Scanner ler = new Scanner(System.in);
		return ler.nextInt();
	}
	
	private double entradaD() {
		@SuppressWarnings("resource")
		Scanner ler = new Scanner(System.in);
		return ler.nextDouble();
	}
	
	public void run() {
		
		while(true) {
			System.out.println("Menu Principal");
			System.out.println("1 - Cadastrar novo produto;");
			System.out.println("2 - Listar produtos;");
			System.out.println("3 - Atualizar estoque dos produtos;");
			System.out.println("4 - Gerar relatorio de vendas.");
			int menu = entradaI();
				
			switch(menu) {
			case 1:
				cadastraProduto();
				break;
			case 2:
				try {
					listarProdutos();
				} catch (IOException e) {
					System.out.println("Erro ao listar produtos. " + e);
				}
				break;
			case 3:
				try {
					atualizaEstoque();
				} catch (IOException e) {
					System.out.println("Erro ao atualizar estoque. " + e);
				}
				break;
			case 4:
				gerarRelatorio();
				break;
			default:
				System.out.println("Erro, tente novamente." + '\n');
				break;
			}
		}
		
	}

	private void cadastraProduto() {
		System.out.print("Nome: ");
		String nome = entradaS();
		System.out.print("Preco: ");
		double preco = entradaD();
		System.out.print("Validade: ");
		String validade = entradaS();
		System.out.print("Fornecedor: ");
		String fornecedor = entradaS();
		System.out.print("Quantidade: ");
		int quantidade = entradaI();
		
		try {
			Produto produto = new Produto();
			produto.cadastro(nome, preco, validade, fornecedor, quantidade);
			System.out.println("Produto cadastrado." + '\n');
		} catch (IOException e) {
			System.out.println("Erro ao criar objeto Produto. " + e);
		}
	}

	private void listarProdutos() throws IOException {
		String[] li = null;
		try {
			CSVReader estoque;
			estoque = lerP();
			System.out.println("Produtos no estoque: ");
			while((li = estoque.readNext()) != null) {
				int quantidade = Integer.parseInt(li[4]);
				if(quantidade > 0) {
					System.out.println("Nome: " + li[0]
										+ " - Preco: " + li[1]
										+ " - Validade: " + li[2]
										+ " - Fornecedor: " + li[3]
										+ " - Quantidade: " + li[4]);
				}
			}
			System.out.print('\n');
		} catch (FileNotFoundException e2) {
			System.out.println("Erro ao abrir arquivo. " + e2);
		}
		
		try {
			CSVReader esgotado;
			esgotado = lerP();
			System.out.println("Produtos esgotados: ");
			while((li = esgotado.readNext()) != null) {
				int quantidade = Integer.parseInt(li[4]);
				if(quantidade == 0) {
					System.out.println("Nome: " + li[0]
										+ " - Preco: " + li[1]
										+ " - Validade: " + li[2]
										+ " - Fornecedor: " + li[3]
										+ " - Quantidade: " + li[4]);
				}
			}
			System.out.print('\n');
		} catch (FileNotFoundException e1) {
			System.out.println("Erro ao abrir arquivo. " + e1);
		}
	}

	private void atualizaEstoque() throws IOException {
		List<String[]> lista = new ArrayList<String[]>();
		int aux = 0;
		
		System.out.print("Digite o nome do produto: ");
		String p = entradaS();
		
		CSVReader buscar = lerP();
		String[] li = null;
		while((li = buscar.readNext()) != null) {
			if(p.equalsIgnoreCase(li[0])) {
				System.out.println("Quantidade atual: " + li[4]);
				System.out.print("Nova quantidade: ");
				int quantidade = entradaI();
				String[] atualiza = (li[0] + "#" + li[1] + "#" + li[2] + "#" + li[3] + "#" + quantidade).split("#");
				lista.add(atualiza);
				aux++;
			} else {
				String[] produto = (li[0] + "#" + li[1] + "#" + li[2] + "#" + li[3] + "#" + li[4]).split("#");
				lista.add(produto);
			}
		}
		
		if(aux == 0) {
			System.out.println("Produto nao encontrado." + '\n');
		} else {
			CSVWriter atualiza = new CSVWriter(new FileWriter("arquivos/produtos.csv"));
			for(String[] temp : lista) {
				atualiza.writeNext(temp, false);
			}
			System.out.println("Lista de produtos atualizada." + '\n');
			atualiza.close();
		}
		buscar.close();
	}

	private void gerarRelatorio() {
		System.out.println("Em construcao.");
	}
	
}
