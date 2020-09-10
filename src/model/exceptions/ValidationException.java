package model.exceptions;

import java.util.HashMap;
import java.util.Map;

//carrega os erros do formulario departments
public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	//config para carregar erros na execess�o de cada campo utilizando a collection Map(chave, valor)
	//exece��o personalizada que carrega uma cole��o contendo todo erros poss�veis
	private Map<String, String> errors = new HashMap<>();
	
	//for�a a insta�ncia��o da exece��o com String passando para a Super Classe msg
	public ValidationException(String msg) {
		super(msg);
	}
	
	//pega os erros da collection erros
	public Map<String, String> getErros() {
		return errors;
	}
	
	//adiciona os erros a cole��o
	public void addError(String fieldName, String errorMessage) {
		errors.put(fieldName, errorMessage);
	}
	
}
