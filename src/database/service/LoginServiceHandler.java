package database.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;

import database.DatabaseServer;

public class LoginServiceHandler implements LoginService.Iface{
	private Connection loginServerDB;
	private Statement loginServerStmt;
	private ResultSet loginServerRS;
	
	private static final Logger logger = LogManager.getLogger(LoginServiceHandler.class);
	public LoginServiceHandler(Connection loginServerDB) throws SQLException{
		this.loginServerDB=loginServerDB;
		loginServerStmt = loginServerDB.createStatement();
	}

	@Override
	public int authorization(String name, String password) throws TException {
		int status = 0;
		try {
			
			loginServerRS = loginServerStmt.executeQuery("SELECT * FROM accounts WHERE login = '"+name+"'AND password = '"+password+"'");
			
			if(loginServerRS.next()){
				
				if(loginServerRS.getInt("accessLevel")!=-1){
				status = 2;
				}else{
					status = -1;
				}
			}else{
				status = 1;
			}
			}catch (Exception e) {
				logger.catching(e);
		}
		
		return status;
	}

	@Override
	public String serverList() throws TException {
		// TODO Auto-generated method stub
		return null;
	}

}
