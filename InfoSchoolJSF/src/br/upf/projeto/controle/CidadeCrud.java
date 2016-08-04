package br.upf.projeto.controle;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import br.upf.casca.ads.beans.uteis.ConexaoJPA;
import br.upf.casca.ads.beans.classes.Cidade;

@ManagedBean
@SessionScoped
public class CidadeCrud {

	private Cidade cidade;
	private List<Cidade> cidades;
	private String[] listaUF = {"AC", "AL","AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", 
			"PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"}; 
		
	public void inicializarLista(){
		EntityManager em = ConexaoJPA.getEntityManager();
		cidades = em.createQuery("from Cidade").getResultList();
		em.close();
	}
	
	public String incluir(){
		cidade = new Cidade();
		return "CidadeForm?faces-redirect=true";
		
	}
	
	public String gravar(){
		EntityManager em = ConexaoJPA.getEntityManager();
		 em.getTransaction().begin();
		 em.merge(cidade);
		 em.getTransaction().commit();
		 em.close();
		 return "CidadeList?faces-redirect=true";
	}
	
	public String cancelar() {
		 return "CidadeList";
	}
	
	public String alterar(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		cidade = em.find(Cidade.class, id);
		em.close();
		return "CidadeForm?faces-redirect=true";
	}

	public String excluir(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		cidade = em.find(Cidade.class, id);
		em.getTransaction().begin();
		em.remove(cidade);
		em.getTransaction().commit();
		em.close();
		return "CidadeList?faces-redirect=true";
	}

	public CidadeCrud() {
		super();
		cidade = new Cidade();
		EntityManager em = ConexaoJPA.getEntityManager();
		cidades = em.createQuery("from Cidade").getResultList();
		em.close();
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public List<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}
	
	public String[] getListaUF() {
		return listaUF;
	}

	public void setListaUF(String[] listaUF) {
		this.listaUF = listaUF;
	}

}
