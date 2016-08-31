package hu.mapro.model.analyzer.test.meta.test1.view;

import hu.mapro.meta.IdProvider;
import hu.mapro.model.analyzer.test.meta.test1.entity.Product;
import hu.mapro.model.analyzer.test.meta.test1.entity.ProductCategory;
import hu.mapro.model.analyzer.test.meta.test1.entity.ProductManager;
import hu.mapro.model.analyzer.test.meta.test1.entity.UserAccount;

public class UserAccountView {

	@IdProvider
	private static UserAccount userAccount;
	
	private static Product product;
	
	String username = userAccount.username();
	
	Boolean isDisabled = userAccount.isDisabled();
	
	ProductCategory productCategory = product.productCategory();
	
	ProductManager productManager = product.productManager();
	
}
