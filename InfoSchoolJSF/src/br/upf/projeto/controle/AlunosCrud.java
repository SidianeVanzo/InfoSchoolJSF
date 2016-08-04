package br.upf.projeto.controle;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import br.upf.casca.ads.beans.classes.Alunos;
import br.upf.casca.ads.beans.classes.Cidade;
import br.upf.casca.ads.beans.classes.TipoAluno;
import br.upf.casca.ads.beans.uteis.ConexaoJPA;


@ManagedBean
@SessionScoped
public class AlunosCrud {

	private Alunos objeto;
	private List<Alunos> alunos;
	

	public List<Cidade> completeCidade(String query) {
		EntityManager em = ConexaoJPA.getEntityManager();
		 List<Cidade> results = em.createQuery(
		 "from Cidade where upper(nome) like "+
		"'"+query.trim().toUpperCase()+"%' "+
		 "order by nome").getResultList();
		 em.close();
		 return results;
		 }
	
	public List<TipoAluno> completeTipoAluno(String query) {
		EntityManager em = ConexaoJPA.getEntityManager();
		 List<TipoAluno> results = em.createQuery(
		 "from TipoAluno where upper(descricao) like "+
		"'"+query.trim().toUpperCase()+"%' "+
		 "order by descricao").getResultList();
		 em.close();
		 return results;
	}

	
	public void inicializarLista() {
		EntityManager em = ConexaoJPA.getEntityManager();
		alunos = em.createQuery("from Alunos").getResultList();
		em.close();
	}

	public String incluir() {
		objeto = new Alunos();
		return "AlunosForm?faces-redirect=true";

	}

	public String gravar() {
		EntityManager em = ConexaoJPA.getEntityManager();
		em.getTransaction().begin();
		em.merge(objeto);
		em.getTransaction().commit();
		em.close();
		return "AlunosList?faces-redirect=true";
	}

	public String cancelar() {
		return "AlunosList";
	}

	public String alterar(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		objeto = em.find(Alunos.class, id);
		em.close();
		return "AlunosForm?faces-redirect=true";
	}

	public String excluir(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		objeto = em.find(Alunos.class, id);
		em.getTransaction().begin();
		em.remove(objeto);
		em.getTransaction().commit();
		em.close();
		return "AlunosList?faces-redirect=true";
	}

	public AlunosCrud() {
		super();
		objeto = new Alunos();
		EntityManager em = ConexaoJPA.getEntityManager();
		alunos = em.createQuery("from Alunos").getResultList();
		em.close();
	}

	public Alunos getObjeto() {
		return objeto;
	}

	public void setObjeto(Alunos objeto) {
		this.objeto = objeto;
	}

	public List<Alunos> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Alunos> alunos) {
		this.alunos = alunos;
	}

	
	
	
}
