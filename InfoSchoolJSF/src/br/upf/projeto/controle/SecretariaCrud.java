package br.upf.projeto.controle;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import br.upf.casca.ads.beans.classes.Secretaria;
import br.upf.casca.ads.beans.uteis.ConexaoJPA;

@ManagedBean
@SessionScoped
public class SecretariaCrud {

	private Secretaria objeto;
	private List<Secretaria> secretarias;
		
	public void inicializarLista(){
		EntityManager em = ConexaoJPA.getEntityManager();
		secretarias = em.createQuery("from Secretaria").getResultList();
		em.close();
	}
	
	public String incluir(){
		objeto = new Secretaria();
		return "SecretariaForm?faces-redirect=true";
		
	}
	
	public String gravar(){
		EntityManager em = ConexaoJPA.getEntityManager();
		 em.getTransaction().begin();
		 objeto.setTipo("SECRETARIA");
		 em.merge(objeto);
		 em.getTransaction().commit();
		 em.close();
		 return "SecretariaList?faces-redirect=true";
	}
	
	public String cancelar() {
		 return "SecretariaList";
	}
	
	public String alterar(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		objeto = em.find(Secretaria.class, id);
		em.close();
		return "SecretariaForm?faces-redirect=true";
	}

	public String excluir(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		objeto = em.find(Secretaria.class, id);
		em.getTransaction().begin();
		em.remove(objeto);
		em.getTransaction().commit();
		em.close();
		return "SecretariaList?faces-redirect=true";
	}

	public SecretariaCrud() {
		super();
		objeto = new Secretaria();
		EntityManager em = ConexaoJPA.getEntityManager();
		secretarias = em.createQuery("from Secretaria").getResultList();
		em.close();
	}

	public Secretaria getObjeto() {
		return objeto;
	}

	public void setObjeto(Secretaria objeto) {
		this.objeto = objeto;
	}

	public List<Secretaria> getSecretarias() {
		return secretarias;
	}

	public void setSecretarias(List<Secretaria> secretarias) {
		this.secretarias = secretarias;
	}

}
