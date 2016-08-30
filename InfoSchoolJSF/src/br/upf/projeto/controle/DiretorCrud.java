package br.upf.projeto.controle;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import br.upf.casca.ads.beans.classes.Diretor;
import br.upf.casca.ads.beans.uteis.ConexaoJPA;

@ManagedBean
@SessionScoped
public class DiretorCrud {

	private Diretor objeto;
	private List<Diretor> diretores;
		
	public void inicializarLista(){
		EntityManager em = ConexaoJPA.getEntityManager();
		diretores = em.createQuery("from Diretor").getResultList();
		em.close();
	}
	
	public String incluir(){
		objeto = new Diretor();
		return "DiretorForm?faces-redirect=true";
		
	}
	
	public String gravar(){
		EntityManager em = ConexaoJPA.getEntityManager();
		 em.getTransaction().begin();

		 objeto.setTipo("DIRETOR");
		 em.merge(objeto);
		 em.getTransaction().commit();
		 em.close();
		 return "DiretorList?faces-redirect=true";
	}
	
	public String cancelar() {
		 return "DiretorList";
	}
	
	public String alterar(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		objeto = em.find(Diretor.class, id);
		em.close();
		return "DiretorForm?faces-redirect=true";
	}

	public String excluir(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		objeto = em.find(Diretor.class, id);
		em.getTransaction().begin();
		em.remove(objeto);
		em.getTransaction().commit();
		em.close();
		return "DiretorList?faces-redirect=true";
	}

	public DiretorCrud() {
		super();
		objeto = new Diretor();
		EntityManager em = ConexaoJPA.getEntityManager();
		diretores = em.createQuery("from Diretor").getResultList();
		em.close();
	}

	public Diretor getObjeto() {
		return objeto;
	}

	public void setObjeto(Diretor objeto) {
		this.objeto = objeto;
	}

	public List<Diretor> getDiretores() {
		return diretores;
	}

	public void setDiretores(List<Diretor> diretores) {
		this.diretores = diretores;
	}

}
