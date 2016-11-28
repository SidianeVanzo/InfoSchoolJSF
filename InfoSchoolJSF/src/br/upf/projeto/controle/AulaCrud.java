package br.upf.projeto.controle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import org.primefaces.context.RequestContext;

import br.upf.casca.ads.beans.classes.AlunosTurma;
import br.upf.casca.ads.beans.classes.Aula;
import br.upf.casca.ads.beans.classes.Chamada;
import br.upf.casca.ads.beans.classes.Turma;
import br.upf.casca.ads.beans.uteis.ConexaoJPA;

@ManagedBean
@SessionScoped
public class AulaCrud {

	private Aula objeto;
	private Turma turma;
	private List<Aula> aulas;
	private List<Chamada> listChamada;
	private String prova;
	
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

	public String incluir(Aula objeto, Turma turma) {
		//this.objeto = objeto;
		this.objeto = new Aula();
		this.turma = turma;
		if(this.objeto ==  null){
			this.objeto = new Aula();
			this.objeto.setChamada(new ArrayList<>());
		}		
		this.objeto.setData(new Date());	
		
		iniciaChamada();
		return "/Cadastros/Aula/AulaForm?faces-redirect=true";		
	}

	public String gravar() {
		EntityManager em = ConexaoJPA.getEntityManager();
		
		objeto.setChamada(listChamada);
		objeto.setTurma(turma);
		
			
		em.getTransaction().begin();
		em.merge(objeto);
		em.getTransaction().commit();
		
		em.close();
		setProva("");
		return "/Cadastros/Turma/TurmaList?faces-redirect=true";
	}

	public String cancelar() {
		return "/Cadastros/Turma/TurmaList?faces-redirect=true";
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

	
	private void iniciaChamada(){
		listChamada = new ArrayList();
		if(turma != null){
			//cada aluno tem a sua chamada de cada aula feita
			//pega um array de alunos que tem na turma e cria um array de chamadas, inserindo um novo
			//aluno em cada nova chamada que é criada.
			List<AlunosTurma> listAlunos = turma.getAlunosTurma();
			for(AlunosTurma aluno : turma.getAlunosTurma()){
				Chamada chamada = new Chamada();
				chamada.setAluno(aluno.getAlunos());
				chamada.setComparecimentoAula(false);				
				listChamada.add(chamada);
			}
		}
	}
	
	//Este método percorre a  lista de chamada e seta o valor do campo prova para todos os objetos 
	//prova das chamadas, ou seja, a descrição da prova vai ficar igual em todas, mesmo eu adicionando
	//apenas em um só lugar.
	public void atualizaProva(){
		for(Chamada ch : listChamada){
			ch.setProvas(prova);
		}
	}
	
	//busca no banco as aulas da turma, diferenciando as turmas através do id que é passado aqui
	//e informado no TurmaList ao Ver Aula
	public String listaAulasTurma(int idTurma){
		EntityManager em = ConexaoJPA.getEntityManager();
		aulas = em.createQuery("from Aula WHERE turma_id = "+idTurma).getResultList();
		em.close();
		return "/Cadastros/Aula/AulaList?faces-redirect=true";
	}
	
	//lista detalhadamente cada aula já realizada. Aula é informada corretamente pois é passada a aula
	//aqui e informada no Aula List ao Ver Mais
	public void listaChamadaAlunos(Aula aula){
		objeto = aula;
		RequestContext.getCurrentInstance().execute("PF('dlgDetail').show()");
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

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public List<Chamada> getListChamada() {
		return listChamada;
	}

	public void setListChamada(List<Chamada> listChamada) {
		this.listChamada = listChamada;
	}

	public String getProva() {
		return prova;
	}

	public void setProva(String prova) {
		this.prova = prova;
	}
}