package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

//classe para Connetar e Desconectar do Banco de Dados

/*####################################################
A DbExeption foi criada para que não precisemos toda
hora tratar execessões no programa, pois ela é
derivada da classe RunTimeExecepiton. Uma vez
que a SQLExeception" é derivada da 'Classe Exeception',
ela nos obriga que ela seja tratada com try/catch
sempre e com a RunTimeException não.
Além disso já teremos também nossa execessão
persolanilzada. 

####################################################*/

public class DB {
	
	//-- start open connection
	//metodo para connectar ao banco de dados
	  //variavel que contem a conexão
	private static Connection conn = null;
	  //metodo que testa e faz a conexão
	public static Connection getConnection() {
		try {
			Properties props = loadProperties();
			String url = props.getProperty("dburl");
			conn = DriverManager.getConnection(url, props);
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		return conn;
	} //-- end open connection
	
	//-- start close connection
	//metodo para fechar a conexão ao banco de dados
	public static void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
			}
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	} //-- end close connection
	
	
	//metodo auxiliar para carregar as propriedade gravadas no arquivo "db.properties".
	private static Properties loadProperties() {
		//try para abrir o arquivo
		try(FileInputStream fs = new FileInputStream("db.properties")) {
			Properties props = new Properties();
			props.load(fs);
			return props;
		}
		catch (IOException e) {
			throw new DbException(e.getMessage());
		}
	}
	
	//close do Statement
	public static void closeStatement(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	//close do ResultSet
	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	//close do PreparedStatement
	public static void closePreparedStatement(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
}
