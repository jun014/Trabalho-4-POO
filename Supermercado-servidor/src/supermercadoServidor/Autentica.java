package supermercadoServidor;

public interface Autentica {
	void setLogin(int id, String senha);
	boolean login(Autentica user, String senha);
	void setInfo(int id, String nome, String email);
}
