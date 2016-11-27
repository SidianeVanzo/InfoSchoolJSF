package br.upf.projeto.controle;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;

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
		List<Curso> results = em.createQuery("from Curso where upper(nome) like " + "'" + query.trim().toUpperCase()
				+ "%' " + listaID() + "order by nome").getResultList();
		em.close();
		return results;
	}

	// metodo para verificar se tem cursos j� adicionados ao professor e, se tiver,
	// n�o mostrar mais o nome dele na op��o de incluir cursos no professor.
	// Com o AND id NOT IN � configurado para n�o pegar os cursos que j� est�o relacionados ao professor
	private String listaID() {
		StringBuilder sb = new StringBuilder();
		boolean primeiro = true;
		try {
			for (Curso c : objeto.getCursosHabilitados()) {
				if (!primeiro) {
					sb.append(",");
				} else {
					sb.append("AND id NOT IN (");
				}
				sb.append(c.getId());
				primeiro = false;
			}
			if (!primeiro) {
				sb.append(")");
			}
		} catch (Exception e) {
		}
		return sb.toString();
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

		objeto.setTipo("PROFESSOR");
		List<Professor> listaUsuario = new ArrayList<Professor>();
		List<Professor> listaEmail = new ArrayList<Professor>();
		// explica��o do processo: AdministradorCrud
		if (objeto.getId() == null) {
			Query qry = em.createQuery("from Pessoa where usuario = :usuario");
			qry.setParameter("usuario", objeto.getUsuario());
			listaUsuario = qry.getResultList();

			Query query = em.createQuery("from Pessoa where email = :email");
			query.setParameter("email", objeto.getEmail());
			listaEmail = query.getResultList();
		}

		if (listaUsuario.isEmpty()) {
			if (listaEmail.isEmpty()) {
				em.merge(objeto);
				em.getTransaction().commit();
				em.close();
				return "ProfessorList?faces-redirect=true";
			} else {
				FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"O e-mail informado j� est� cadastrado no sistema. Por favor, informe outro e-mail!!", "");
				FacesContext.getCurrentInstance().addMessage(null, mensagem);
				return "";
			}
		} else {
			FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"O usu�rio informado j� est� cadastrado no sistema. Por favor, informe outro usu�rio!!", "");
			FacesContext.getCurrentInstance().addMessage(null, mensagem);
			return "";
		}
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

	// no m�todo excluir, o professor apenas perde seu usu�rio e senha, mas o
	// cadastro continua salvo,
	// para que ele ainda apare�a nas turmas j� existentes.
	public String excluir(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		objeto = em.find(Professor.class, id);
		objeto.setUsuario(" ");
		objeto.setSenha(" ");
		em.getTransaction().begin();
		em.merge(objeto);
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
		// cursos = new Curso();
	}

	public void excluirItem(Integer rowIndex) {
		objeto.getCursosHabilitados().remove(rowIndex.intValue()); // exclui
																	// cursos da
																	// cole��o
	}

	public void gravarItem() {
		if (this.rowIndex == null) {
			objeto.getCursosHabilitados().add(cursos); // adiciona cursos na
														// cole��o
		} else {
			objeto.getCursosHabilitados().set(rowIndex, cursos); // altera na
																	// cole��o
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
