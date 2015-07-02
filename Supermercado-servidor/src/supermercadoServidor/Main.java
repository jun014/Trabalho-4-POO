package supermercadoServidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * Main da aplicação do servidor de um sistema de supermercado.
 * 
 * Desenvolvido pelo aluno: Anderson Jun Morikawa; número USP: 8936989
 */

public class Main {

	public static void main(String[] args) throws IOException {
		
		/*
		 * Criação da thread que é responsável pelo gerenciamento do sistema.
		 */
		AdmSupermercado adm = new AdmSupermercado();
		Thread thread = new Thread(adm);
		thread.start();
		
		@SuppressWarnings("resource")
		ServerSocket servidor = new ServerSocket(Integer.parseInt(args[0]));
		/*
		 * Loop responsável pela criação das threads do cliente,
		 * a cada requisito de conexão com o servidor é criado uma thread.
		 */
		while(true) {
			Socket cliente = servidor.accept();
			ConexaoCliente cc = new ConexaoCliente(cliente);
			Thread t = new Thread(cc);
			t.start();
		}

	}

}
