package br.upf.projeto.controle;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import br.upf.casca.ads.beans.classes.Alunos;
import br.upf.casca.ads.beans.classes.Curso;
import br.upf.casca.ads.beans.classes.Pessoa;
import br.upf.casca.ads.beans.classes.Professor;
import br.upf.casca.ads.beans.classes.Questionario;
import br.upf.casca.ads.beans.uteis.ConexaoJPA;

@ManagedBean
@SessionScoped
public class QuestionarioCrud {

	private Questionario objeto;
	private List<Questionario> questionarios;
	private String[] listaTipoQuest = {"Durante o Curso", "Final do Curso"}; 
		
	public void inicializarLista(){
		EntityManager em = ConexaoJPA.getEntityManager();
		questionarios = em.createQuery("from Questionario").getResultList();
		em.close();
	}
	
	public List<Professor> completeProfessor(String query) {
		EntityManager em = ConexaoJPA.getEntityManager();
		 List<Professor> results = em.createQuery(
		 "from Professor where upper(nome) like "+
		"'"+query.trim().toUpperCase()+"%' "+
		 "order by nome").getResultList();
		 em.close();
		 return results;
	}
	
	public List<Curso> completeCurso(String query) {
		EntityManager em = ConexaoJPA.getEntityManager();
		 List<Curso> results = em.createQuery(
		 "from Curso where upper(nome) like "+
		"'"+query.trim().toUpperCase()+"%' "+
		 "order by nome").getResultList();
		 em.close();
		 return results;
	}
	
	public List<Alunos> completeAlunos(String query) {
		EntityManager em = ConexaoJPA.getEntityManager();
		 List<Alunos> results = em.createQuery(
		 "from Alunos where upper(nome) like "+
		"'"+query.trim().toUpperCase()+"%' "+
		 "order by nome").getResultList();
		 em.close();
		 return results;
	}
	
	public String incluir(){
		objeto = new Questionario();
		return "QuestionarioForm?faces-redirect=true";
		
	}
	
	public String gravar(){
		EntityManager em = ConexaoJPA.getEntityManager();
		 em.getTransaction().begin();
		 em.merge(objeto);
		 em.getTransaction().commit();
		 em.close();
		 return "QuestionarioList?faces-redirect=true";
	}
	
	public String cancelar() {
		 return "QuestionarioList";
	}
	
	public String alterar(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		objeto = em.find(Questionario.class, id);
		em.close();
		return "QuestionarioForm?faces-redirect=true";
	}

	public String excluir(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		objeto = em.find(Questionario.class, id);
		em.getTransaction().begin();
		em.remove(objeto);
		em.getTransaction().commit();
		em.close();
		return "QuestionarioList?faces-redirect=true";
	}

	public QuestionarioCrud() {
		super();
		objeto = new Questionario();
		EntityManager em = ConexaoJPA.getEntityManager();
		questionarios = em.createQuery("from Questionario").getResultList();
		em.close();
	}

	public Questionario getObjeto() {
		return objeto;
	}

	public void setObjeto(Questionario objeto) {
		this.objeto = objeto;
	}

	public List<Questionario> getQuestionarios() {
		return questionarios;
	}

	public void setQuestionarios(List<Questionario> questionarios) {
		this.questionarios = questionarios;
	}

	public String[] getListaTipoQuest() {
		return listaTipoQuest;
	}

	public void setListaTipoQuest(String[] listaTipoQuest) {
		this.listaTipoQuest = listaTipoQuest;
	}


}
