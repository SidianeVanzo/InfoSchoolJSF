package br.upf.projeto.controle;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import br.upf.casca.ads.beans.classes.Alunos;
import br.upf.casca.ads.beans.classes.Aula;
import br.upf.casca.ads.beans.classes.Curso;
import br.upf.casca.ads.beans.classes.Professor;
import br.upf.casca.ads.beans.classes.Turma;
import br.upf.casca.ads.beans.uteis.ConexaoJPA;

@ManagedBean
@SessionScoped
public class AulaCrud {

	private Aula objeto;
	private List<Aula> aulas;

	public List<Turma> completeTurma(String query) {
		EntityManager em = ConexaoJPA.getEntityManager();
		List<Turma> results = em.createQuery(
				"from Turma where upper(nome) like " + "'" + query.trim().toUpperCase() + "%' " + "order by nome")
				.getResultList();
		em.close();
		return results;
	}


	public void inicializarLista() {
		EntityManager em = ConexaoJPA.getEntityManager();
		aulas = em.createQuery("from Aula").getResultList();
		em.close();
	}

	public String incluir() {
		objeto = new Aula();
		return "AulaForm?faces-redirect=true";

	}

	public String gravar() {
		EntityManager em = ConexaoJPA.getEntityManager();
		em.getTransaction().begin();
		em.merge(objeto);
		em.getTransaction().commit();
		em.close();
		return "AulaList?faces-redirect=true";
	}

	public String cancelar() {
		return "AulaList";
	}

	public String alterar(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		objeto = em.find(Aula.class, id);
		em.close();
		return "AulaForm?faces-redirect=true";
	}

	public String excluir(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		objeto = em.find(Aula.class, id);
		em.getTransaction().begin();
		em.remove(objeto);
		em.getTransaction().commit();
		em.close();
		return "AulaList?faces-redirect=true";
	}

	public AulaCrud() {
		super();
		objeto = new Aula();
		EntityManager em = ConexaoJPA.getEntityManager();
		aulas = em.createQuery("from Aula").getResultList();
		em.close();
	}

	public List<Aula> getAulas() {
		return aulas;
	}

	public void setAulas(List<Aula> aulas) {
		this.aulas = aulas;
	}

	public Aula getObjeto() {
		return objeto;
	}

	public void setObjeto(Aula objeto) {
		this.objeto = objeto;
	}

}
