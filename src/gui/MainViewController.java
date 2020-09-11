package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;
import model.services.SellerService;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemSeller;
	@FXML
	private MenuItem menuItemDepartment;
	@FXML
	private MenuItem menuItemAbout;
	
	
	//metodos
	@FXML
	public void onMenuItemSellerAction() {
		loadView("/gui/SellerList.fxml",
			    (SellerListController controller) -> {
			      controller.setSellerService(new SellerService());
			      controller.updateTableview();}
			    );
	}
	@FXML
			//Segundo parametro contem a funcão para inicializar o controlador DepartmentListController
			//como uma expressão lambda. Necessário declara o 2o parametro na funcao loadview com a
			//Interface Consumer
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml",
			    (DepartmentListController controller) -> {
			      controller.setDepartmentService(new DepartmentService());
			      controller.updateTableview();}
			    );
	}
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {}); //função X que leva em nada, pois a tela nao carrega nada
	}
	
	
	
	//Initializable Session
	@Override
	public void initialize(URL location, ResourceBundle resources) {
			
	}
	
	//carrega as telas com a Função Generica LoadView
	//Consumer utilizado para utilizar apenas uma funcão LoadView. A torna genérica
	private synchronized <T> void loadView(String absolutName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
			VBox newVBox = loader.load();
			
			//coloca a tela about dentro da tela principal. Necessário pegar referencia 
			//da Scene da classe Main
			Scene mainScene = Main.getMainScene();
			//Acessando os filhos do VBOX
			VBox mainVBox = (VBox)((ScrollPane)mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			//comando para ativar o segundo parameto Consumer
			T controller = loader.getController();
			initializingAction.accept(controller);
			
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	
}
