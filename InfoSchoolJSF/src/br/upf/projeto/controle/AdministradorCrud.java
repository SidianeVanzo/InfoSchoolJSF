package br.upf.projeto.controle;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import br.upf.casca.ads.beans.classes.Administrador;
import br.upf.casca.ads.beans.uteis.ConexaoJPA;

@ManagedBean
@SessionScoped
public class AdministradorCrud {

	private Administrador objeto;
	private List<Administrador> administradores;
		
	public void inicializarLista(){
		EntityManager em = ConexaoJPA.getEntityManager();
		administradores = em.createQuery("from Administrador").getResultList();
		em.close();
	}
	
	public String incluir(){
		objeto = new Administrador();
		return "AdministradorForm?faces-redirect=true";
		
	}
	
	public String gravar(){
		EntityManager em = ConexaoJPA.getEntityManager();
		 em.getTransaction().begin();
		 em.merge(objeto);
		 em.getTransaction().commit();
		 em.close();
		 return "AdministradorList?faces-redirect=true";
	}
	
	public String cancelar() {
		 return "AdministradorList";
	}
	
	public String alterar(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		objeto = em.find(Administrador.class, id);
		em.close();
		return "AdministradorForm?faces-redirect=true";
	}

	public String excluir(Integer id) {
		EntityManager em = ConexaoJPA.getEntityManager();
		objeto = em.find(Administrador.class, id);
		em.getTransaction().begin();
		em.remove(objeto);
		em.getTransaction().commit();
		em.close();
		return "AdministradorList?faces-redirect=true";
	}

	public AdministradorCrud() {
		super();
		objeto = new Administrador();
		EntityManager em = ConexaoJPA.getEntityManager();
		administradores = em.createQuery("from Administrador").getResultList();
		em.close();
	}

	public Administrador getObjeto() {
		return objeto;
	}

	public void setObjeto(Administrador objeto) {
		this.objeto = objeto;
	}

	public List<Administrador> getAdministradores() {
		return administradores;
	}

	public void setAdministradores(List<Administrador> administradores) {
		this.administradores = administradores;
	}

}
