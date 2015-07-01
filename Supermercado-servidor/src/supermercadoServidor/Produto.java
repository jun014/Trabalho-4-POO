package supermercadoServidor;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/*
 *Classe que faz o registro dos produtos no servidor. 
 */

public class Produto {
	private String nome;
	private double preco;
	private String validade;
	private String fornecedor;
	private int quantidade;
	
	CSVReader ler;
	CSVWriter escrever;
	
	Produto() throws IOException {
		ler = new CSVReader(new FileReader("arquivos/produtos.csv"));
		escrever = new CSVWriter(new FileWriter("arquivos/produtos.csv", true));
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setPreco(double preco) {
		this.preco = preco;
	}
	
	public void setValidade(String validade) {
		this.validade = validade;
	}
	
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}
	
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public String getNome() {
		return nome;
	}

	public double getPreco() {
		return preco;
	}

	public String getValidade() {
		return validade;
	}

	public String getFornecedor() {
		return fornecedor;
	}
	
	public int getQuantidade() {
		return quantidade;
	}
	
	public void setInfo(String nome, double preco, String validade, String fornecedor, int quantidade) {
		setNome(nome);
		setPreco(preco);
		setValidade(validade);
		setFornecedor(fornecedor);
		setQuantidade(quantidade);
	}
	
	public void cadastro(String nome, double preco, String validade, String fornecedor, int quantidade) throws IOException {
		String[] dados = (nome + "#"
							+ preco + "#"
							+ validade + "#"
							+ fornecedor + "#"
							+ quantidade).split("#");
		escrever.writeNext(dados, false);
		escrever.close();
	}
	
}
