package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener {

	// referencias para os componetes da tela DepartmentList
	// variaveis de referencia
	@FXML
	private TableView<Department> tableViewDepartment;
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	@FXML
	private TableColumn<Department, String> tableColumnName;
	@FXML
	private TableColumn<Department, Department> tableColumnEDIT;
	@FXML
	private TableColumn<Department, Department> tableColumnREMOVE;

	@FXML
	private Button btnNew;
	// atributito para Carregar os departamentos
	private ObservableList<Department> obsList;

	// declarando dependencias
	private DepartmentService service; // = new DepartmentService(); - Isso acarretaria em forte acoplamento

	// getter e setter
	// metodo set para inversão de controle e injetar a dependencia "new
	// DepartmentService()"
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	// Metodos
	public void onBtnNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		// formulario começa vázio e passa o obj vazio para o formulario
		Department obj = new Department();
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
	}

	// metodo resposável por acesser o serviço, carregar os departamentos e jogar
	// os departamentos na ObservableList. Apartir dai se associa a TableView
	// ao ObservableList para os departamentos apareçam na tela
	public void updateTableview() {
		if (service == null) {
			throw new IllegalStateException("Service was null!");
		}
		List<Department> list = service.findAll(); // recupera os departamentos do servico
		obsList = FXCollections.observableArrayList(list); // instancia o observableList e carrega a lista dentro o
															// obsList
		tableViewDepartment.setItems(obsList); // carra os item para a tela
		initEditButtons();
		initRemoveButtons();
	}

	// Initializable
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Metodo auxiliara para iniciar algum componente na tela
		initializeNodes();
	}

	// comandos para iniciar apropriadademente o compartamento das colunas na tabela
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id")); // id- nome da variavel da Classe
																				// Department
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));// name- nome da variavel da Classe
																				// Department

		// ajustar a TableViewDepartment no palco(stage)
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());

	}

	// função para carregar a janela departmentForm
	private void createDialogForm(Department obj, String absolutName, Stage parentStage) {
		try {
			// logica para abrir a janela de formulario
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
			Pane pane = loader.load();

			// injeta o obj no DepartmentListController
			// pega o controlador da tela carregada (formulario)
			DepartmentFormController controller = loader.getController();
			// carrega o objeto no formulario
			controller.setDepartment(obj);
			// injeçao de dependência
			controller.setDepartmentService(new DepartmentService());
			// inscrevendo este objeto para ser um listener no evento "DataChangeListener"
			controller.subscribeDataChangeListner(this);
			controller.updateFormData();

			// para colocar uma janela modal na frente de outra é necessário instanciar um
			// novo Stage
			// um palco na frente do outro
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department Data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading View", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	// (observer/listener)
	// inscreve este objeto "DepartmentFormController" para ser um listener no
	// evento ""
	// atualiza os dados da tabela Departmentlis atraves do evento de atulaizacao
	// for recebido
	public void onDataChanged() {
		updateTableview();

	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Department obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to Delete?");
		
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was Null");
			}
			try {
				service.remove(obj);
				updateTableview();
			}
			catch (DbIntegrityException e) {
				Alerts.showAlert("ERROR Removing Object", null, e.getMessage(), AlertType.ERROR);
			}
		}		
	}
}
