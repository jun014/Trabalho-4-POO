package supermercadoServidor;

import java.util.ArrayList;
import java.util.List;

/*
 *Classe com o registro das vendas feitas pelo cliente. 
 */

public class Vendas {
	private Usuario usuario;
	private List<String[]> lista;
	private double total;
	
	Vendas() {
		lista = new ArrayList<String[]>();
		total = 0;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public void addProduto(String[] registro) {
		lista.add(registro);
	}
	
	public void setTotal(double item) {
		this.total += item;
	}
	
	public Usuario getUsuario() {
		return this.usuario;
	}
	
	public List<String[]> getLista() {
		return this.lista;
	}
	
	public double getTotal() {
		return this.total;
	}
}
