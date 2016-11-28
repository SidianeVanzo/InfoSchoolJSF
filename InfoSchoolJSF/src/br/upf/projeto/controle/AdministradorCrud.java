package br.upf.projeto.controle;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.upf.casca.ads.beans.classes.Administrador;
import br.upf.casca.ads.beans.uteis.ConexaoJPA;

@ManagedBean
@SessionScoped
public class AdministradorCrud {

	private Administrador objeto;
	//cria-se uma lista do objeto para que seja possível apresentar todos os administradores no AdministradorList
	private List<Administrador> administradores;
		
	public void inicializarLista(){
		EntityManager em = ConexaoJPA.getEntityManager();
		administradores = em.createQuery("from Administrador  order by nome DESC").getResultList();
		em.close();
	}
	
	public String incluir(){
		objeto = new Administrador();
		return "AdministradorForm?faces-redirect=true";
		
	}
	
	public String gravar(){
		EntityManager em = ConexaoJPA.getEntityManager();
		 em.getTransaction().begin();

		 //seta o tipo da pessoa no código para que a pessoa não precise ficar informando em todo cadastro
		 objeto.setTipo("ADMINISTRADOR");
		 List<Administrador> listaUsuario = new ArrayList<Administrador>();
		 List<Administrador> listaEmail = new ArrayList<Administrador>();
			
			if (objeto.getId() == null) {
				Query qry = em.createQuery("from Pessoa where usuario = :usuario");
				qry.setParameter("usuario", objeto.getUsuario());
				listaUsuario = qry.getResultList();
				
				Query query = em.createQuery("from Pessoa where email = :email");
				query.setParameter("email", objeto.getEmail());
				listaEmail = query.getResultList();
			}
			//realiza uma busca para verificar se os campos usuário e email informados já estão cadastrados
			//no sistema. Se sim, uma mensagem é informada avisando o usuário de que não é possível cadastrar
			//pois já existe esta mesma informação no banco. Para validar isso, é necessário verificar se
			//a lista de usuários e e-mails está vazia. Se sim, quer dizer que não tem usuários e email
			//iguais cadastrados no banco
			if (listaUsuario.isEmpty()) {
				if(listaEmail.isEmpty()){
						em.merge(objeto);
						em.getTransaction().commit();
						em.close();
						return "AdministradorList?faces-redirect=true";
				}else{
					FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"O e-mail informado já está cadastrado no sistema. Por favor, informe outro e-mail!!", "");
					FacesContext.getCurrentInstance().addMessage(null, mensagem);
					return "";
				}
			} else {
				FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"O usuário informado já está cadastrado no sistema. Por favor, informe outro usuário!!", "");
				FacesContext.getCurrentInstance().addMessage(null, mensagem);
				return "";
			}
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
