package br.upf.projeto.controle;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import br.upf.casca.ads.beans.classes.TipoTurma;
import br.upf.casca.ads.beans.uteis.ConexaoJPA;

@ManagedBean
@SessionScoped
public class TipoTurmaCrud {

	private TipoTurma objeto;
	private List<TipoTurma> tipoTurmas;
	

	public void inicializarLista() {
		EntityManager em = ConexaoJPA.getEntityManager();
		tipoTurmas = em.createQuery("from TipoTurma").getResultList();
		em.close();
	}

	public String incluir() {
		objeto = new TipoTurma();
		return "TipoTurmaForm?faces-redirect=true";

	}

	public String gravar() {
		EntityManager em = ConexaoJPA.getEntityManager();
		em.getTransaction().begin();
		em.merge(objeto);
		em.getTransaction().commit();
		em.close();
		return "TipoTurmaList?faces-redirect=true";
	}

	public String cancelar() {
		return "TipoTurmaList";
	}

	public String alterar(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		objeto = em.find(TipoTurma.class, id);
		em.close();
		return "TipoTurmaForm?faces-redirect=true";
	}

	public String excluir(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		objeto = em.find(TipoTurma.class, id);
		em.getTransaction().begin();
		em.remove(objeto);
		em.getTransaction().commit();
		em.close();
		return "TipoTurmaList?faces-redirect=true";
	}

	public List<TipoTurma> getTipoTurmas() {
		return tipoTurmas;
	}

	public void setTipoTurmas(List<TipoTurma> tipoTurmas) {
		this.tipoTurmas = tipoTurmas;
	}

	public TipoTurma getObjeto() {
		return objeto;
	}

	public void setObjeto(TipoTurma objeto) {
		this.objeto = objeto;
	}

	public TipoTurmaCrud() {
		super();
		objeto= new TipoTurma();
		EntityManager em = ConexaoJPA.getEntityManager();
		tipoTurmas = em.createQuery("from TipoTurma").getResultList();
		em.close();
	}
}
