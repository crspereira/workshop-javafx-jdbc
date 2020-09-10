package model.exceptions;

import java.util.HashMap;
import java.util.Map;

//carrega os erros do formulario departments
public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	//config para carregar erros na execessão de cada campo utilizando a collection Map(chave, valor)
	//execeção personalizada que carrega uma coleção contendo todo erros possíveis
	private Map<String, String> errors = new HashMap<>();
	
	//força a instaânciação da execeção com String passando para a Super Classe msg
	public ValidationException(String msg) {
		super(msg);
	}
	
	//pega os erros da collection erros
	public Map<String, String> getErros() {
		return errors;
	}
	
	//adiciona os erros a coleção
	public void addError(String fieldName, String errorMessage) {
		errors.put(fieldName, errorMessage);
	}
	
}
