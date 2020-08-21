package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.entities.Department;
import model.entities.Seller;

public class DataBaseTest2 {

	public static void main(String[] args) {
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			conn = DB.getConnection();
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
				   + "FROM seller INNER JOIN department "
				   + "ON seller.DepartmentId = department.Id "
				   + "WHERE seller.Id = ?");
			
			st.setInt(1, 3); //1 para o primeiro interrogação
			//resultado da consulta SQL em formato de tabela é armazenado na variável rs
			rs = st.executeQuery(); //aponta como padrão para a posição O, não contem objeto necessário instanciar 
			
			//criando/instanciando o objeto "Seller" associado ao "Department" na memória
			if (rs.next()) { //testa se a consulta retornou algum registro
				
				//instanciando e setando o Objeto Department
				Department dep = new Department();
				dep.setId(rs.getInt("DepartmentId"));
				dep.setName(rs.getString("DepName"));
				
				//instanciando e setando o Objeto Seller
				Seller obj = new Seller();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				obj.setEmail(rs.getString("Email"));
				obj.setBaseSalary(rs.getDouble("BaseSalary"));
				obj.setBirthDate(rs.getDate("BirthDate"));
				obj.setDepartment(dep); //traz o objeto department instanciado inteiro
				
				System.out.println(obj);
			}

			System.out.println("Nulo!");
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}
