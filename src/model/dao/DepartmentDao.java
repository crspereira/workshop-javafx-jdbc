package model.dao;

import java.util.List;

//Classe respons�vel por definir as opera��oes de acesso a dados (Acesso ao Banco de Dados) relacinado a Sellers
//Classe definida como Interface para dar flexibilidade e preserva��o do Contrato de objetos de acesso a dados
import model.entities.Department;

//Classe respons�vel por definir as opera��oes de acesso a dados (Acesso ao Banco de Dados) relacionado a Department
//Classe definida como Interface para dar flexibilidade e preserva��o do Contrato de objetos de acesso a dados
public interface DepartmentDao {
	
	public void insert(Department obj);
	public void update(Department obj);
	public void deleteById(Integer id);
	public Department findById(Integer id); //retorna o ID ou Null
	public List<Department> findAll(); //retorna a lista de Depertamentos
	
}
