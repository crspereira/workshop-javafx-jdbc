package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {
	
	private SellerDao dao = DaoFactory.createSellerDao();
	
	//metodo
	public List<Seller> findAll() {
		
		return dao.findAll();
		
		/*//MOCK - Retorna dados fictícios - Lista Temporaria - Nao vem do banco de dados
		List<Seller> list = new ArrayList<>();
		list.add(new Seller(1, "Books"));
		list.add(new Seller(2, "Computers"));
		list.add(new Seller(3, "Electronics"));
		return list;*/	
	}
	
	public void saveOrUpdate(Seller obj) {
		//null significa save, novo department se nao atualiza
		if (obj.getId() == null) { 
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void remove(Seller obj) {
		dao.deleteById(obj.getId());
	}
}
