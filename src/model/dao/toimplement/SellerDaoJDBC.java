package model.dao.toimplement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

//Classe responsável por implementar as Interfaces SellerDAO
//com a tecnologia JDBC
public class SellerDaoJDBC implements SellerDao {

	// criando DEPENDENCIA do SellerDaoJDBC com a CONEXÃO ao Banco de Dados
	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	//

	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO seller " + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES " + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			// executa o sql acima
			int rowsAffected = st.executeUpdate();
			// testa se algo foi inserido. Set pega o id e o grava no obj
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) { // se existe pega id gerado
					int id = rs.getInt(1);
					obj.setId(id); // grava o id
				}
				DB.closeResultSet(rs);

			} else {
				throw new DbException("Unexpected erro! No rows affected!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE seller SET " + "Name = ?, " + "Email = ?, " + "BirthDate = ?, "
					+ "BaseSalary = ?, " + "DepartmentId = ? " + "WHERE Id = ?");

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());

			//executa o sql acima
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM seller " + "WHERE Id = ?");
			
			//recebe o id como paramentro de restrição
			st.setInt(1, id);

			//executa o sql acima
			int rows = st.executeUpdate();
			
			if (rows == 0) {
				throw new DbException("ID not found!");
			}
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE seller.Id = ?");

			st.setInt(1, id); // 1 para o primeiro interrogação
			// resultado da consulta SQL em formato de tabela é armazenado na variável rs
			rs = st.executeQuery(); // aponta como padrão para a posição O, não contem objeto necessário instanciar

			// criando/instanciando o objeto "Seller" associado ao "Department" na memória
			if (rs.next()) { // testa se a consulta retornou algum registro

				// instanciando e setando o Objeto Department atravéz da função
				// "instantiateDepartment"
				Department dep = instantiateDepartment(rs);
				// instanciando e setando o Objeto Seller atravéz da função "instantiateSeller"
				Seller obj = instantiateSeller(rs, dep);

				return obj;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	// função auxiliar que instancia o objeto Seller
	// ajuda da Reutilização do código muito verboso
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep); // traz o objeto department instanciado inteiro
		return obj;
	}

	// função auxiliar que instancia o objeto Department
	// ajuda da Reutilização do código muito verboso
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "ORDER BY Name");

			// resultado da consulta SQL em formato de tabela é armazenado na variável rs
			rs = st.executeQuery(); // aponta como padrão para a posição O, não contem objeto necessário instanciar

			// criando uma lista para gravar os Vendores e Departamentos
			List<Seller> list = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<>(); // Map para armazenar objeto department

			// criando/instanciando o objetos "Seller" associado ao mesmo "Department" na
			// memória
			while (rs.next()) { // testa se a consulta retornou algum registro

				// pega departmentId dentro do map
				Department dep = map.get(rs.getInt("DepartmentId"));

				// caso não exista o departmentId no map se instancia um e o grava no map
				if (dep == null) {
					// instanciando e setando o Objeto Department atravéz da função
					// "instantiateDepartment"
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}

				// instanciando e setando o Objeto Seller atravéz da função "instantiateSeller"
				Seller obj = instantiateSeller(rs, dep);

				// gravando na lista
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE DepartmentId = ? " + "ORDER BY Name");

			st.setInt(1, department.getId()); // 1 para o primeiro interrogação
			// resultado da consulta SQL em formato de tabela é armazenado na variável rs
			rs = st.executeQuery(); // aponta como padrão para a posição O, não contem objeto necessário instanciar

			// criando uma lista para gravar os Vendores e Departamentos
			List<Seller> list = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<>(); // Map para armazenar objeto department

			// criando/instanciando o objetos "Seller" associado ao mesmo "Department" na
			// memória
			while (rs.next()) { // testa se a consulta retornou algum registro

				// pega departmentId dentro do map
				Department dep = map.get(rs.getInt("DepartmentId"));

				// caso não exista o departmentId no map se instancia um e o grava no map
				if (dep == null) {
					// instanciando e setando o Objeto Department atravéz da função
					// "instantiateDepartment"
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}

				// instanciando e setando o Objeto Seller atravéz da função "instantiateSeller"
				Seller obj = instantiateSeller(rs, dep);

				// gravando na lista
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
