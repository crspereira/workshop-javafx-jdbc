package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

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
	  //atributito para Carregar os departamentos
	private ObservableList<Department> obsList;
	
	  //declarando dependencias
	private DepartmentService service; //= new DepartmentService(); - Isso acarretaria em forte acoplamento
	
	//getter e setter
	//metodo set para inversão de controle e injetar a dependencia "new DepartmentService()"
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	
	//Metodos
	public void onBtnNewAction() {
		System.out.println("onBtnNewAction");
	}

	 //metodo resposável por acesser o serviço, carregar os departamentos e jogar
	 //os departamentos na ObservableList. Apartir dai se associa a TableView
	 //ao ObservableList para os departamentos apareçam na tela
	public void updateTableview() {
		if (service == null) {
			throw new IllegalStateException("Service was null!");
		}
		List<Department> list = service.findAll(); //recupera os departamentos do servico
		obsList = FXCollections.observableArrayList(list); //instancia o observableList e carrega a lista dentro o obsList
		tableViewDepartment.setItems(obsList); //carra os item para a tela
	}
	
	
	//Initializable
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Metodo auxiliara para iniciar algum componente na tela
		initializeNodes();
	}
	
	//comandos para iniciar apropriadademente o compartamento das colunas na tabela
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id")); //id- nome da variavel da Classe Department
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));//name- nome da variavel da Classe Department
		
		//ajustar a TableViewDepartment no palco(stage)
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
		
	}

}
