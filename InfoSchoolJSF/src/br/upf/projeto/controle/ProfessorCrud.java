package br.upf.projeto.controle;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import br.upf.casca.ads.beans.classes.Curso;
import br.upf.casca.ads.beans.classes.Professor;
import br.upf.casca.ads.beans.uteis.ConexaoJPA;

@ManagedBean
@SessionScoped
public class ProfessorCrud {
	private Professor objeto;
	private List<Professor> professores;
	

	public List<Curso> completeCurso(String query) {
		EntityManager em = ConexaoJPA.getEntityManager();
		 List<Curso> results = em.createQuery(
		 "from Curso where upper(nome) like "+
		"'"+query.trim().toUpperCase()+"%' "+
		 "order by nome").getResultList();
		 em.close();
		 return results;
		 }

	
	public void inicializarLista() {
		EntityManager em = ConexaoJPA.getEntityManager();
		professores = em.createQuery("from Professor").getResultList();
		em.close();
	}

	public String incluir() {
		objeto = new Professor();
		return "ProfessorForm?faces-redirect=true";

	}

	public String gravar() {
		EntityManager em = ConexaoJPA.getEntityManager();
		em.getTransaction().begin();
		em.merge(objeto);
		em.getTransaction().commit();
		em.close();
		return "ProfessorList?faces-redirect=true";
	}

	public String cancelar() {
		return "ProfessorList";
	}

	public String alterar(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		objeto = em.find(Professor.class, id);
		em.close();
		return "ProfessorForm?faces-redirect=true";
	}

	public String excluir(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		objeto = em.find(Professor.class, id);
		em.getTransaction().begin();
		em.remove(objeto);
		em.getTransaction().commit();
		em.close();
		return "ProfessorList?faces-redirect=true";
	}

	public ProfessorCrud() {
		super();
		objeto = new Professor();
		EntityManager em = ConexaoJPA.getEntityManager();
		professores = em.createQuery("from Professor").getResultList();
		em.close();
	}


	public Professor getObjeto() {
		return objeto;
	}


	public void setObjeto(Professor objeto) {
		this.objeto = objeto;
	}


	public List<Professor> getProfessores() {
		return professores;
	}


	public void setProfessores(List<Professor> professores) {
		this.professores = professores;
	}

	
	private Curso cursos; // cursos em edi��o, vinculado ao formul�rio
	private Integer rowIndex = null; // �ndice do cursos selecionado - alterar e
										// excluir

	public void incluirItem() {
		rowIndex = null;
		//cursos = new Curso();
	}

	public void excluirItem(Integer rowIndex) {
		objeto.getCursosHabilitados().remove(rowIndex.intValue()); // exclui cursos da cole��o
	}

	public void gravarItem() {
		if (this.rowIndex == null) {
			objeto.getCursosHabilitados().add(cursos); // adiciona cursos na cole��o
		} else {
			objeto.getCursosHabilitados().set(rowIndex, cursos); // altera na cole��o
		}
		rowIndex = null;
		cursos = null;
	}

	public void cancelarItem() {
		rowIndex = null;
		cursos = null;
	}


	public Curso getCursos() {
		return cursos;
	}


	public void setCursos(Curso cursos) {
		this.cursos = cursos;
	}


	public Integer getRowIndex() {
		return rowIndex;
	}


	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}

	
}