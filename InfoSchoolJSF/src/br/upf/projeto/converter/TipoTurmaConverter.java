package br.upf.projeto.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;

import br.upf.casca.ads.beans.classes.TipoTurma;
import br.upf.casca.ads.beans.uteis.ConexaoJPA;

@FacesConverter(value = "tipoTurmaConverter")
public class TipoTurmaConverter implements Converter{
	@Override
	public TipoTurma getAsObject(FacesContext fc, UIComponent uic, String value) {
		if (value != null && value.trim().length() > 0) {
			try {
				EntityManager em = ConexaoJPA.getEntityManager();
				TipoTurma ret = em.find(TipoTurma.class, Integer.parseInt(value));
				em.close();
				return ret;
			} catch (NumberFormatException e) {
				throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Erro de Convers�o do TipoTurma", "TipoTurma inv�lido."));
			}
		} else
			return null;
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object object) {
		if (object != null) {
			return String.valueOf(((TipoTurma) object).getId());
		} else
			return null;
	}
}
