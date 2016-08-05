package br.upf.projeto.controle;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import br.upf.casca.ads.beans.classes.Alunos;
import br.upf.casca.ads.beans.classes.PergRespQuest;
import br.upf.casca.ads.beans.uteis.ConexaoJPA;

@ManagedBean
@SessionScoped
public class PergRespQuestCrud {

	private PergRespQuest objeto;
	private List<PergRespQuest> pergRespQuests;
	
	public void inicializarLista(){
		EntityManager em = ConexaoJPA.getEntityManager();
		pergRespQuests = em.createQuery("from PergRespQuest").getResultList();
		em.close();
	}
	
	public String incluir(){
		objeto = new PergRespQuest();
		return "PergRespQuestForm?faces-redirect=true";
		
	}
	
	public String gravar(){
		EntityManager em = ConexaoJPA.getEntityManager();
		 em.getTransaction().begin();
		 em.merge(objeto);
		 em.getTransaction().commit();
		 em.close();
		 return "PergRespQuestList?faces-redirect=true";
	}
	
	public String cancelar() {
		 return "PergRespQuestList";
	}
	
	public String alterar(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		objeto = em.find(PergRespQuest.class, id);
		em.close();
		return "PergRespQuestForm?faces-redirect=true";
	}

	public String excluir(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		objeto = em.find(PergRespQuest.class, id);
		em.getTransaction().begin();
		em.remove(objeto);
		em.getTransaction().commit();
		em.close();
		return "PergRespQuestList?faces-redirect=true";
	}

	public PergRespQuestCrud() {
		super();
		objeto = new PergRespQuest(); 
		EntityManager em = ConexaoJPA.getEntityManager();
		pergRespQuests = em.createQuery("from PergRespQuest").getResultList();
		em.close();
	}

	public PergRespQuest getObjeto() {
		return objeto;
	}

	public void setObjeto(PergRespQuest objeto) {
		this.objeto = objeto;
	}

	public List<PergRespQuest> getPergRespQuests() {
		return pergRespQuests;
	}

	public void setPergRespQuests(List<PergRespQuest> pergRespQuests) {
		this.pergRespQuests = pergRespQuests;
	}

}
