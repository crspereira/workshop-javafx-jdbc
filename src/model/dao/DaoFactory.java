package model.dao;

import db.DB;
import model.dao.toimplement.DepartmentDaoJDBC;
import model.dao.toimplement.SellerDaoJDBC;

//Classe responsável por Instanciar as EntitiesDAO utilizando métodos Statics das diferentes
//   tecnologias, que por sua vez, disperá a necessidade de instânciação no programa principal.
//A classe irá expor um método que retornará o tipo da Interface, mais internamente ela irá
//   instânciar uma implementação.
//Macete para evita expor a implementação, expõe apenas a interface que é genérica.
//Forma de injetar dependecias sem explicitar a implementação

public class DaoFactory {
	
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
	}

	public static DepartmentDao createDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}
}
