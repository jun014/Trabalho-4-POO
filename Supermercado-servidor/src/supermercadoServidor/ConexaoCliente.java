package supermercadoServidor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/*
 *Classe com a interação servidor-cliente. 
 */

public class ConexaoCliente implements Runnable {
	
	Socket socket;
	BufferedReader entradaString;
	PrintWriter saidaString;
	DataInputStream entradaInt;
	DataOutputStream saidaInt;
	boolean logado = false;
	Usuario usuario;
	Vendas venda;
	String clienteArq;
	
	private String csvArqUsuario = "arquivos/usuarios.csv";
	private String csvArqProduto = "arquivos/produtos.csv";
	
	public ConexaoCliente(Socket cliente) throws IOException {
		socket = cliente;
		try {
			entradaString = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
			saidaString = new PrintWriter(cliente.getOutputStream(), true);
			entradaInt = new DataInputStream(cliente.getInputStream());
			saidaInt = new DataOutputStream(cliente.getOutputStream());
		} catch(IOException e) {
			System.out.println("Erro classe cc: " + e);
			socket.close();
		}
	}
	
	public void run() {
		try {
			menu();
		} catch (IOException e) {
			System.out.println("Erro no menu principal: " + e);
		}
	}
	
	private void menu() throws IOException {
		int menu = entradaInt.readInt();
		
		switch(menu) {
		case 1:
			acesso();
			sistemaInterno();
			break;
		case 2:
			cadastroUsuario();
			break;
		case 3:
			socket.close();
			break;
		default:
			System.out.println("erro.");
			break;
		}
	
		if(menu != 3) {
			menu();
		}
	}
	
	private void acesso() throws IOException {
		while(!logado) {
			CSVReader ler = new CSVReader(new FileReader(csvArqUsuario));
			String[] li = null;
			try {
				String idLogin = entradaString.readLine();
				String senhaLogin = entradaString.readLine();
				int idLogin1 = Integer.parseInt(idLogin);
				while((li = ler.readNext()) != null) {
					int id = Integer.parseInt(li[0]);
					if(id == idLogin1) {
						usuario = new Usuario();
						usuario.setLogin(id, li[1]);
						logado = usuario.login(usuario, senhaLogin);
						if(logado) {
							saidaInt.writeInt(1);
							usuario.setInfo(id, li[2], li[5]);
							
							venda = new Vendas();
							venda.setUsuario(usuario);
							clienteArq = usuario.getNome() + ".csv";
							
							break;
						} else {
							saidaInt.writeInt(0);
						}
					}
				}
			} catch (IOException e) {
				System.out.println("Erro login: " + e);
				socket.close();
				break;
			}
			ler.close();
		}
	}
	
	private void cadastroUsuario() throws IOException {
		try {
			String nome = entradaString.readLine();
			String endereco = entradaString.readLine();
			String telefone = entradaString.readLine();
			String email = entradaString.readLine();
			String senha = entradaString.readLine();
			Usuario usuario = new Usuario();
			usuario.novoUsuario(nome, endereco, telefone, email, senha);
			usuario.cadastro(usuario);
			saidaInt.writeInt(usuario.getID());
		} catch (IOException e) {
			System.out.println("cadastro usuario erro: " + e);
		}
	}
	
	private void sistemaInterno() throws IOException {
		saidaString.println(usuario.getNome());
		int menu = entradaInt.readInt();
		
		switch(menu) {
		case 1:
			listaProdutos();
			break;
		case 2:
			carrinhoCompras();
			break;
		case 3:
			this.logado = false;
			break;
		default:
			break;
		}
		
		if(menu != 3) {
			sistemaInterno();
		}
	}
	
	private void listaProdutos() throws IOException {
		CSVReader ler = new CSVReader(new FileReader(csvArqProduto));
		List<String[]> lista = ler.readAll();
		int total = lista.size();
		saidaInt.writeInt(total);
		
		CSVReader ler2 = new CSVReader(new FileReader(csvArqProduto));
		String[] li = null;
		
		while((li = ler2.readNext()) != null) {
			int quantidade = Integer.parseInt(li[4]);
			if(quantidade > 0) {
				saidaString.println("Nome: " + li[0]
									+ " - Preco: " + li[1]
									+ " - Validade: " + li[2]
									+ " - Fornecedor: " + li[3]
									+ " - Quantidade: " + li[4]);
			} else {
				saidaString.println("Nome: " + li[0]
						+ " - Preco: " + li[1]
						+ " - Validade: " + li[2]
						+ " - Fornecedor: " + li[3]
						+ " - Quantidade: " + li[4] + " ->ESGOTADO");
			}
		}
		ler.close();
		ler2.close();
	}
	
	private void carrinhoCompras() {
		
		int menu;
		try {
			menu = entradaInt.readInt();
		
		switch(menu) {
		case 1:
			listar();
			break;
		case 2:
			venda();
			break;
		case 3:
			totalAPagar();
			break;
		case 4:
			break;
		default:
			System.out.println("erro.");
		}
		} catch (IOException e) {
			System.out.println("Erro no menu venda: " + e);
		}
	}

	private void listar() throws IOException {
		int tamanhoLista = venda.getLista().size();
		saidaInt.writeInt(tamanhoLista);

		if(tamanhoLista == 0) {
			//lista vazia.
		} else {
			for(String[] temp : venda.getLista()) {
				saidaString.println(Arrays.toString(temp));
			}
		}
		
	}
	
	private void venda() throws IOException {
		CSVWriter reg = new CSVWriter(new FileWriter("arquivos/" + clienteArq, true));
		
		CSVReader ler = new CSVReader(new FileReader(csvArqProduto));
		String produto = entradaString.readLine();
		String[] li = null;
		int aux = 0;
		
		while((li = ler.readNext()) != null) {
			saidaInt.writeBoolean(true);
			if(produto.equalsIgnoreCase(li[0])) {
				saidaInt.writeBoolean(true);
				saidaString.println(li[0]);		//nome
				saidaString.println(li[1]);		//preco
				saidaString.println(li[2]);		//validade
				saidaString.println(li[3]);		//produtor
				saidaString.println(li[4]);		//quantidade

				String valida = entradaString.readLine();
				if(valida.equalsIgnoreCase("sim")) {
					int produtoQtd = Integer.parseInt(li[4]);
					int quantidade = entradaInt.readInt();
					if(quantidade <= produtoQtd) {
						String[] dados = (li[0] + "#"
										+ li[3] + "#"
										+ quantidade + "#"
										+ li[1]).split("#");
						reg.writeNext(dados, false);
						venda.addProduto(dados);
						aux++;
						atualizaEstoque(li[0], (produtoQtd - quantidade));
						double preco = Double.parseDouble(li[1]);
						venda.setTotal((preco * quantidade));
						saidaInt.writeBoolean(true);
						ler.close();
						reg.close();
						break;
					} else {
						saidaInt.writeBoolean(false);
						aux++;
						ler.close();
						reg.close();
						break;
					}
				}
			} else {
				saidaInt.writeBoolean(false);
			}
		}
		//System.out.println();
		saidaInt.writeBoolean(false);
		saidaInt.writeBoolean(false);
		saidaInt.writeInt(aux);
		ler.close();
	}
	
	private void totalAPagar() throws IOException {
		saidaInt.writeDouble(venda.getTotal());
	}
	
	private void atualizaEstoque(String produto, int quantidade) throws IOException {
		List<String[]> lista = new ArrayList<String[]>();
		
		CSVReader buscar = new CSVReader(new FileReader(csvArqProduto));
		
		String[] li = null;
		while((li = buscar.readNext()) != null) {
			if(produto.equalsIgnoreCase(li[0])) {
				String[] atualiza = (li[0] + "#" + li[1] + "#" + li[2] + "#" + li[3] + "#" + quantidade).split("#");
				lista.add(atualiza);
			} else {
				String[] p = (li[0] + "#" + li[1] + "#" + li[2] + "#" + li[3] + "#" + li[4]).split("#");
				lista.add(p);
			}
		}
		
		CSVWriter atualiza = new CSVWriter(new FileWriter("arquivos/produtos.csv"));
		for(String[] temp : lista) {
			atualiza.writeNext(temp, false);
		}
		atualiza.close();
		buscar.close();
	}

}
