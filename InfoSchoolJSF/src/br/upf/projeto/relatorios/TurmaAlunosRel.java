package br.upf.projeto.relatorios;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import br.upf.casca.ads.beans.classes.Alunos;
import br.upf.casca.ads.beans.classes.AlunosTurma;
import br.upf.casca.ads.beans.classes.Turma;
import br.upf.casca.ads.beans.uteis.ConexaoJPA;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@ManagedBean
@RequestScoped
public class TurmaAlunosRel {

	private Alunos alunos;

	public void executaRelatorio() {

		try {
			HashMap parameters = new HashMap();
			parameters.put("ALUNOS", alunos.getId());
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.responseComplete();
			ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
			Connection con = ConexaoJPA.getEntityManagerJDBCConnection();
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					scontext.getRealPath("/WEB-INF/Relatorios/TurmaAlunos/TurmaAlunos.jasper"), parameters, con);
			byte[] b = JasperExportManager.exportReportToPdf(jasperPrint);
			HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
			res.setContentType("application/pdf");
			res.setHeader("Content-disposition", "inline;filename=arquivo.pdf"); // diretamente na página
			// res.setHeader("Content-disposition",
			// "attachment;filename=arquivo.pdf");// baixar ou salvar
			//um programa que precise escrever um dado em algum local (destino) precisa de um OutputStream ou um Writer.
			res.getOutputStream().write(b);
			res.getCharacterEncoding();
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Alunos> completeAlunos(String query) {
		EntityManager em = ConexaoJPA.getEntityManager();
		List<Alunos> results = em.createQuery(
				"from Alunos where upper(nome) like " + "'" + query.trim().toUpperCase() + "%' " + "order by nome")
				.getResultList();
		em.close();
		return results;
	}

	public Alunos getAlunos() {
		return alunos;
	}

	public void setAlunos(Alunos alunos) {
		this.alunos = alunos;
	}
}
