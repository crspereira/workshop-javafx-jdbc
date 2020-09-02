package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

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
	private DepartmentService service;
	//armazena o evento de atualização da lista do departamento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>(); 
	
	//metodos
	@FXML
	public void onBtnSaveAction(ActionEvent event) {
		//preve esquecimento de injeção de dependencia
		if (entity == null) {
			throw new IllegalStateException("Entity is Null!");
		}
		if (service == null) {
			throw new IllegalStateException("Service is Null!");
		}
		try {
			entity = getFormatData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners(); //notifica a atualização da lista
			Utils.currentStage(event).close();
		}
		catch (DbException e) {
			Alerts.showAlert("Error Saving Object", null, e.getMessage(), AlertType.ERROR);
		}
	}	
		//emissão do evento de atualização da lista
		private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
		
	}
		private Department getFormatData() {
			Department obj = new Department();
			
			obj.setId(Utils.tryParseToInt(txtId.getText()));
			obj.setName(txtName.getText());
			
			return obj;
		}
		
	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
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
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	//(subject/emissor)
	//Lista de eventos
	public void subscribeDataChangeListner(DataChangeListener listener) {
		dataChangeListeners.add(listener);
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
