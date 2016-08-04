package br.upf.projeto.controle;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import br.upf.casca.ads.beans.classes.TipoAluno;
import br.upf.casca.ads.beans.uteis.ConexaoJPA;

@ManagedBean
@SessionScoped
public class TipoAlunoCrud {
	private TipoAluno objeto;
	private List<TipoAluno> tipoAlunos;
	

	public void inicializarLista() {
		EntityManager em = ConexaoJPA.getEntityManager();
		tipoAlunos = em.createQuery("from TipoAluno").getResultList();
		em.close();
	}

	public String incluir() {
		objeto = new TipoAluno();
		return "TipoAlunoForm?faces-redirect=true";

	}

	public String gravar() {
		EntityManager em = ConexaoJPA.getEntityManager();
		em.getTransaction().begin();
		em.merge(objeto);
		em.getTransaction().commit();
		em.close();
		return "TipoAlunoList?faces-redirect=true";
	}

	public String cancelar() {
		return "TipoAlunoList";
	}

	public String alterar(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		objeto = em.find(TipoAluno.class, id);
		em.close();
		return "TipoAlunoForm?faces-redirect=true";
	}

	public String excluir(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		objeto = em.find(TipoAluno.class, id);
		em.getTransaction().begin();
		em.remove(objeto);
		em.getTransaction().commit();
		em.close();
		return "TipoAlunoList?faces-redirect=true";
	}

	public List<TipoAluno> getTipoAlunos() {
		return tipoAlunos;
	}

	public void setTipoAlunos(List<TipoAluno> tipoAlunos) {
		this.tipoAlunos = tipoAlunos;
	}

	public TipoAluno getObjeto() {
		return objeto;
	}

	public void setObjeto(TipoAluno objeto) {
		this.objeto = objeto;
	}

	public TipoAlunoCrud() {
		super();
		objeto= new TipoAluno();
		EntityManager em = ConexaoJPA.getEntityManager();
		tipoAlunos = em.createQuery("from TipoAluno").getResultList();
		em.close();
	}
	
	
	
}
