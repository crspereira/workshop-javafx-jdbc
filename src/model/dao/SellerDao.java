package model.dao;

import java.util.List;

import model.entities.Department;
//Classe responsável por definir as operaçãoes de acesso a dados (Acesso ao Banco de Dados) relacinado a Sellers
//Classe definida como Interface para dar flexibilidade e preservação do Contrato de objetos de acesso a dados
import model.entities.Seller;

//Classe responsável por definir as operaçãoes de acesso a dados (Acesso ao Banco de Dados) relacionado ao Seller
//Classe definida como Interface para dar flexibilidade e preservação do Contrato de objetos de acesso a dados
public interface SellerDao {
	
	public void insert(Seller obj);
	public void update(Seller obj);
	public void deleteById(Integer id);
	public Seller findById(Integer id); //retorna o ID ou Null
	public List<Seller> findAll(); //retorna a lista de Vendedores e seus Departamentos
	public List<Seller> findByDepartment(Department department); //Assinatura do metodo que retorna a lista de vendedores por departamentos
	 
}
