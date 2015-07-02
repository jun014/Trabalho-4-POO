package supermercadoCliente;

import java.io.IOException;

/*
 * Main da aplicação do cliente de uma sistema de supermercado.
 * 
 * Criado pelo aluno: Anderson Jun Morikawa; número USP: 8936989
 */

public class Main {

	public static void main(String[] args) {
		
		Cliente cliente;
		try {
			cliente = new Cliente();
			cliente.rodar();
		} catch (IOException e) {
			System.out.println("Erro ao tentar criar Objeto cliente. " + e);
		}

	}

}
