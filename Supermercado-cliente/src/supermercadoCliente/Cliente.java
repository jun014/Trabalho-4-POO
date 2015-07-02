package supermercadoCliente;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/*
 *Classe que faz a interação cliente-servidor. 
 */

public class Cliente {

	Socket socket;
	BufferedReader entradaString;
	PrintWriter saidaString;
	DataInputStream entradaInt;
	DataOutputStream saidaInt;
	
	public Cliente() throws UnknownHostException, IOException {
		String endereco = "127.0.0.1";
		
		socket = new Socket(endereco, 12345);
		entradaString = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		saidaString = new PrintWriter(socket.getOutputStream(), true);
		entradaInt = new DataInputStream(socket.getInputStream());
		saidaInt = new DataOutputStream(socket.getOutputStream());
		
	}
	
	public String entradaS() {
		@SuppressWarnings("resource")
		Scanner ler = new Scanner(System.in);
		return ler.nextLine();
	}
	
	public int entradaI() {
		@SuppressWarnings("resource")
		Scanner ler = new Scanner(System.in);
		return ler.nextInt();
	}
	
	public void rodar() throws IOException {
		
		int menu = menu();
		
		saidaInt.writeInt(menu);
		
		switch(menu) {
		case 1:
			login();
			sistemaInterno();
			break;
		case 2:
			cadastroUsuario();
			break;
		case 3:
			System.out.println("Programa finalizado.");
			socket.close();
			break;
		default:
			System.out.println("Erro.");
			break;
		}
		
		if(menu != 3) {
			rodar();
		}
	}

	private int menu() {
		System.out.println("Bem vindo ao sistema Supermercado!" + '\n'
							+ "Faca o login para ter acesso ao sistema." + '\n'
							+ "Se voce ainda nao tem cadastro no sistema, faca agora mesmo na opcao 2 do menu." + '\n');
		System.out.println("1 - Ja sou cadastrado;");
		System.out.println("2 - Fazer cadastro de novo usuario;");
		System.out.println("3 - Sair do programa.");
		int menu = entradaI();
		return menu;
		
	}
	
	private void login() {
		String idLogin;
		String senhaLogin;
		while(true) {
			System.out.print("ID: ");
			idLogin = entradaS();

			System.out.print("Senha: ");
			senhaLogin = entradaS();
			
			saidaString.println(idLogin);
			saidaString.println(senhaLogin);
			
			int verifica;
			try {
				verifica = entradaInt.readInt();
				if(verifica == 1) {
					System.out.println("Logado com sucesso." + '\n');
					break;
				} else if(verifica == 0) {
					System.out.println("Id ou senha incorreto, tente novamente.");
				}
			} catch (IOException e) {
				System.out.println("Erro ao tentar fazer o login. " + e);
			}
		}
	}

	private void cadastroUsuario() throws IOException {
		
		System.out.print("Nome: ");
		String nome = entradaS();
		saidaString.println(nome);
			
		System.out.print("Endereco: ");
		String endereco = entradaS();
		saidaString.println(endereco);
			
		System.out.print("Telefone: ");
		String telefone = entradaS();
		saidaString.println(telefone);
			
		System.out.print("E-mail: ");
		String email = entradaS();
		saidaString.println(email);
			
		System.out.print("Senha: ");
		String senha = entradaS();
		saidaString.println(senha);
		
		System.out.println("Cadastro efetuado.");
		System.out.println(nome + ", seu ID: " + entradaInt.readInt() + ". Com esse numero voce faz o login no sistema." + '\n');
		}
	
	private void sistemaInterno() throws IOException {
		System.out.println("Bem vindo ao sistema, " + entradaString.readLine() + '\n');
		System.out.println("Menu do usuario");
		System.out.println("1 - Lista de produtos;");
		System.out.println("2 - Meu carrinho de compras;");
		System.out.println("3 - Fazer logoff.");
		int menu = entradaI();
		saidaInt.writeInt(menu);
		
		switch(menu) {
		case 1:
			listaProdutos();
			break;
		case 2:
			carrinhoCompras();
			break;
		case 3:
			System.out.println("Sessao encerrada." + '\n');
			break;
		default:
			System.out.println("Erro, tente novamente." + '\n');
			break;
		}
		
		if(menu != 3) {
			sistemaInterno();
		}
	}
	
	private void listaProdutos() {
		int estoque;
		try {
			estoque = entradaInt.readInt();
			for(int i=0; i<estoque; i++) {
				System.out.println(entradaString.readLine());
			}
		} catch (IOException e) {
			System.out.println("Erro ao ler a lista de produtos do servidor. " + e);
		}
		System.out.print('\n');
	}
	
	private void carrinhoCompras() {
		System.out.println("Menu do carrinho de compras.");
		System.out.println("1 - Visualizar carrinho de compras;");
		System.out.println("2 - Adicionar produto;");
		System.out.println("3 - Total a pagar;");
		System.out.println("4 - Voltar para o menu anterior.");
		int menu = entradaI();
		try {
			saidaInt.writeInt(menu);
			switch(menu) {
			case 1:
				listarCompras();
				break;
			case 2:
				compra();
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
			System.out.println("Erro no menu carrinhoCompras: " + e);
		}
		
	}
	
	private void listarCompras() throws IOException {
		int tamanhoLista = entradaInt.readInt();
		if(tamanhoLista == 0) {
			System.out.println("Sem produtos no carrinho de compras." + '\n');
		} else {
			for(int i=0; i<tamanhoLista; i++) {
				System.out.println(entradaString.readLine());
			}
		}
		System.out.print('\n');
	}
	
	private void compra() throws IOException {
		System.out.print("Digite o nome do produto: ");
		String produto = entradaS();
		saidaString.println(produto);
		boolean loop = true;
		while(loop) {
			loop = entradaInt.readBoolean();
			boolean achou = entradaInt.readBoolean();
			if(achou == true) {
				System.out.println("Nome: " + entradaString.readLine());
				System.out.println("Preco: " + entradaString.readLine());
				System.out.println("Validade: " + entradaString.readLine());
				System.out.println("Produtor: " + entradaString.readLine());
				System.out.println("Quantidade: " + entradaString.readLine());
				
				System.out.println("Deseja realizar a compra desse produto? (sim/nao)");
				String valida = entradaS();
				saidaString.println(valida);
				if(valida.equalsIgnoreCase("sim")) {
					System.out.println("Quantos produtos deseja comprar?");
					int quantidade = entradaI();
					saidaInt.writeInt(quantidade);
					if(entradaInt.readBoolean() == true) {
						System.out.println("Compra registrada." + '\n');
						loop = entradaInt.readBoolean();
						achou = entradaInt.readBoolean();
					} else if(entradaInt.readBoolean() == false){
						System.out.println("Quantidade insuficiente no estoque." + '\n');
						loop = entradaInt.readBoolean();
						achou = entradaInt.readBoolean();
					}
				}
			}
		}
		System.out.println();
		if(entradaInt.readInt() == 0) {
			System.out.println("Produto nao encontrado no estoque." + '\n');
		}
	}
	
	private void totalAPagar() throws IOException {
		System.out.println("Total a pagar: " + entradaInt.readDouble());
	}
	
}
