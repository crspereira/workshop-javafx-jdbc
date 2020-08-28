package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	//metodo
	public List<Department> findAll() {
		
		return dao.findAll();
		
		/*//MOCK - Retorna dados fictícios - Lista Temporaria - Nao vem do banco de dados
		List<Department> list = new ArrayList<>();
		list.add(new Department(1, "Books"));
		list.add(new Department(2, "Computers"));
		list.add(new Department(3, "Electronics"));
		return list;*/	
	}
	
	public void saveOrUpdate(Department obj) {
		//null significa save, novo department se nao atualiza
		if (obj.getId() == null) { 
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
}
