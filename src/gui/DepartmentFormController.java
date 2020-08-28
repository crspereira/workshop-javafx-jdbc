package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.text.html.parser.Entity;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable {
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private Label labelErrorName;
	@FXML
	private Button btnSave;
	@FXML
	private Button btnCancel;
	
	//dependencias
	private Department entity;
	
	//metodos
	@FXML
	public void onBtnSaveAction() {
		System.out.println("onBtnSaveAction");
	}
	@FXML
	public void onBtnCancelAction() {
		System.out.println("onBtnCancelAction");
	}
	
	public void updateFormData() {
		//progamacao defenciva. valida de entity esta valendo nulo, ou seja o objeto não foi injetado
		if (entity == null) {
			throw new IllegalStateException("Entity is Null!");
		}
		txtId.setText(String.valueOf(entity.getId())); //Valueof: converte inteiro para string
		txtName.setText(entity.getName());
	}
	
	//getter e setter
	//set para injeção de dependencia
	public void setDepartment (Department entity) {
		this.entity = entity;
	}
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}

}
