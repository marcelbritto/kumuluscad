package br.com.marcelbritto.kumuluscad.util;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

@FacesConverter("genericConverter")
public class GenericConverter implements Converter {
	
	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
		return value != null && this.getAttributesFrom(component).get(value) != null ? this.getAttributesFrom(component).get(value) : null;
	}
	
	public String getAsString(FacesContext ctx, UIComponent component, Object value) {
		
		if (value != null && !"".equals(value)) {
			try {
				Object id = value.getClass().getMethod("getId", null).invoke(value, null);
				if (id != null && StringUtils.isNotBlank(String.valueOf(id))) {
					this.addAttribute(component, String.valueOf(id), value);
					return String.valueOf(id);
				}else{
					this.addAttribute(component, String.valueOf(value.hashCode()), value);
					return String.valueOf(value.hashCode());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return value != null ? String.valueOf(value) : null;
	}
	
	protected void addAttribute(UIComponent component, String label, Object o) {
		this.getAttributesFrom(component).put(label, o);
	}
	
	protected Map<String, Object> getAttributesFrom(UIComponent component) {
		return component.getAttributes();
	}
}
