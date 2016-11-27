package br.upf.projeto.controle;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import br.upf.casca.ads.beans.classes.Alunos;
import br.upf.casca.ads.beans.classes.AlunosTurma;
import br.upf.casca.ads.beans.classes.Curso;
import br.upf.casca.ads.beans.classes.Professor;
import br.upf.casca.ads.beans.classes.Turma;
import br.upf.casca.ads.beans.uteis.ConexaoJPA;

@ManagedBean
@SessionScoped
public class TurmaCrud {

	private Turma objeto;
	private List<Turma> turmas;
	private String[] listaTipo = { "NORMAL", "PARTICULAR" };

	public List<Curso> completeCurso(String query) {
		EntityManager em = ConexaoJPA.getEntityManager();
		List<Curso> results = em.createQuery(
				"from Curso where upper(nome) like " + "'" + query.trim().toUpperCase() + "%' " + "order by nome")
				.getResultList();
		em.close();
		return results;
	}

	//neste m�todo � setado para apenas mostrar professores que tenham sua senha e usu�rio maior de 0,
	//ou seja, os  professores desativados n�o aparecem pq seu usu�rio e senha s�o excluidos
	public List<Professor> completeProfessor(String query) {
		EntityManager em = ConexaoJPA.getEntityManager();
		List<Professor> results = em
				.createQuery("from Professor where upper(nome) like " + "'" + query.trim().toUpperCase() + "%' "
						+ "AND length(trim(senha)) > 0 AND length(trim(usuario)) > 0" + "order by nome")
				
				.getResultList();
		em.close();
		return results;
	}

	//neste m�todo � feita uma valida��o em rela��o a turma particular (ela deve aceitar apenas 2 alunos)
	//para isso, tem o if que controla. Caso o usu�rio tente cadastrar mais que duas, os alunos n�o estar�o
	//mais disponiveis para serem adicionados.
	//Caso seja apenas 2 alunos na particular ou mesmo a turma normal, � feito uma consulta no banco para
	//mostrar apenas alunos que n�o estejam cancelados
	//o m�todo listaID chamado aqui � para fazer a valida��o de n�o deixar adicionar na turma o mesmo 
	//aluno mais que uma vez
	public List<Alunos> completeAlunos(String query) {
		if (objeto.getTipoTurma().equals("PARTICULAR") && objeto.getAlunosTurma().size() >= 2) {
			return new ArrayList();
		} else {
			EntityManager em = ConexaoJPA.getEntityManager();
			List<Alunos> results = em.createQuery("from Alunos where upper(nome) like " + "'"
					+ query.trim().toUpperCase() + "%' AND tipoaluno <> 'CANCELADO' " + listaID() + "order by nome").getResultList();
			em.close();
			return results;
		}
	}

	public void inicializarLista() {
		EntityManager em = ConexaoJPA.getEntityManager();
		turmas = em.createQuery("from Turma").getResultList();
		em.close();
	}

	public String incluir() {
		objeto = new Turma();
		objeto.setAlunosTurma(new ArrayList<>());
		return "TurmaForm?faces-redirect=true";

	}

	public String gravar() {
		EntityManager em = ConexaoJPA.getEntityManager();
		em.getTransaction().begin();
		em.merge(objeto);
		em.getTransaction().commit();
		em.close();
		return "TurmaList?faces-redirect=true";
	}

	public String cancelar() {
		return "TurmaList";
	}

	public String alterar(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		objeto = em.find(Turma.class, id);
		em.close();
		return "TurmaForm?faces-redirect=true";
	}

	public String excluir(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		objeto = em.find(Turma.class, id);
		em.getTransaction().begin();
		em.remove(objeto);
		em.getTransaction().commit();
		em.close();
		return "TurmaList?faces-redirect=true";
	}

	//metodo para verificar se tem alunos j� adicionados na turma e, se tive, n�o mostrar mais o nome dele
	//na op��o de incluir alunos na turma
	//com o AND id NOT IN � configurado para n�o pegar os j� existentes na turma
	private String listaID() {
		StringBuilder sb = new StringBuilder();
		boolean primeiro = true;
		try {
			for (AlunosTurma at : objeto.getAlunosTurma()) {
				if (!primeiro) {
					sb.append(",");
				} else {
					sb.append("AND id NOT IN (");
				}
				sb.append(at.getAlunos().getId());
				primeiro = false;
			}
			if (!primeiro) {
				sb.append(")");
			}
		} catch (Exception e) {
		}
		return sb.toString();
	}
	
	//m�todo para finalizar turma ao inv�s ed exclu�-la
	public void finalizarTurma(int id){

		EntityManager em = ConexaoJPA.getEntityManager();
		Turma turma = em.find(Turma.class, id); 
		turma.setTipoTurma("FINALIZADA");
		em.getTransaction().begin();
		em.merge(turma);
		em.getTransaction().commit();
		em.close();
	}

	public TurmaCrud() {
		super();
		objeto = new Turma();
		EntityManager em = ConexaoJPA.getEntityManager();
		turmas = em.createQuery("from Turma").getResultList();
		em.close();
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}

	public Turma getObjeto() {
		return objeto;
	}

	public void setObjeto(Turma objeto) {
		this.objeto = objeto;
	}

	public String[] getListaTipo() {
		return listaTipo;
	}

	public void setListaTipo(String[] listaTipo) {
		this.listaTipo = listaTipo;
	}

	private AlunosTurma alunosTurma; // itens em edi��o, vinculado ao formul�rio
	private Integer rowIndex = null; // �ndice dos itens selecionado - alterar e
										// excluir

	public void incluirItem() {
		rowIndex = null;
		alunosTurma = new AlunosTurma();
	}

	public void alterarItem(Integer rowIndex) {
		this.rowIndex = rowIndex;
		alunosTurma = objeto.getAlunosTurma().get(rowIndex); // pega itens da
																// cole��o
	}

	public void excluirItem(Integer rowIndex) {
		objeto.getAlunosTurma().remove(rowIndex.intValue()); // exclui itens da
																// cole��o
	}

	public void gravarItem() {

		if (this.rowIndex == null) {
			alunosTurma.setTurma(objeto);
			objeto.getAlunosTurma().add(alunosTurma); // adiciona itens na
														// cole��o
		} else {
			objeto.getAlunosTurma().set(rowIndex, alunosTurma); // altera na
																// cole��o
		}
		rowIndex = null;
		alunosTurma = null;

	}

	public void cancelarItem() {
		rowIndex = null;
		alunosTurma = null;
	}

	public AlunosTurma getAlunosTurma() {
		return alunosTurma;
	}

	public void setAlunosTurma(AlunosTurma alunosTurma) {
		this.alunosTurma = alunosTurma;
	}

	public Integer getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}

}
