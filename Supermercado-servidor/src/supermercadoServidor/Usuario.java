package supermercadoServidor;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/*
 * Classe que faz o registro do usuario no servidor.
 */

public class Usuario {

	private int id;
	private String senha;
	private String nome;
	private String endereco;
	private String telefone;
	private String email;
	
	String csvArq = "arquivos/usuarios.csv";
	
	public boolean login(Usuario usuario, String senha) {
		if((this.senha).equals(senha)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setLogin(int id, String senha) {
		this.id = id;
		this.senha = senha;
	}
	
	public void setInfo(int id, String nome, String email) {
		this.id = id;
		this.nome = nome;
		this.email = email;
	}
	
	public void setID() throws IOException {
		CSVReader ler = new CSVReader(new FileReader(csvArq));
		List<String[]> lista = ler.readAll();
		this.id = lista.size();
		ler.close();
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public String getEndereco() {
		return this.endereco;
	}
	
	public String getTelefone() {
		return this.telefone;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	private String getSenha() {
		return this.senha;
	}
	
	public void novoUsuario(String nome, String endereco, String telefone, String email, String senha) throws IOException {
		this.setID();
		this.nome = nome;
		this.endereco = endereco;
		this.telefone = telefone;
		this.email = email;
		this.senha = senha;
	}
	
	public void cadastro(Usuario usuario) throws IOException {
		CSVWriter escrever = new CSVWriter(new FileWriter(csvArq, true));
		String[] dados = (usuario.getID() + "#"
						+ usuario.getSenha() + "#"
						+ usuario.getNome() + "#"
						+ usuario.getEndereco() + "#"
						+ usuario.getTelefone() + "#"
						+ usuario.getEmail()).split("#");
		escrever.writeNext(dados, false);
		escrever.close();
	}
	
}
