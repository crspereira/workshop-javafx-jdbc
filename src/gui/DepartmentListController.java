package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable{
	
	//referencias para os componetes da tela DepartmentList
	//variaveis de referencia
	@FXML
	private TableView<Department> tableViewDepartment;
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	@FXML
	private TableColumn<Department, String> tableColumnName;
	@FXML
	private Button btnNew;
	
	
	//Metodos
	public void onBtnNewAction() {
		System.out.println("onBtnNewAction");
	}
	   //comandos para iniciar apropriadademente o compartamento das colunas na tabela
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("ID"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("NAME"));
		
		//ajustar a TableViewDepartment no palco(stage)
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
		
	}

	
	
	//Initializable
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Metodo auxiliara para iniciar algum componente na tela
		initializeNodes();
	}



	

}
