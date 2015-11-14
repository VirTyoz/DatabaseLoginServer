package database.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;

public class LoginServiceHandler implements LoginService.Iface{
	private Connection loginServerDB;
	private Statement loginServerStmt;
	private ResultSet loginServerRS;
	public volatile ServerList serverList;
	
	private static final Logger logger = LogManager.getLogger(LoginServiceHandler.class);
	public LoginServiceHandler(Connection loginServerDB) throws SQLException{
		this.loginServerDB=loginServerDB;
		loginServerStmt = loginServerDB.createStatement();
		serverList =  new ServerList();
	}

	public int authorization(String login, String password) throws TException {
		int status = 0;
		try {
			
			loginServerRS = loginServerStmt.executeQuery("SELECT * FROM accounts WHERE login = '"+login+"'AND password = '"+password+"'");
			
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
	public ServerList serverList() throws TException {
		try {
			loginServerRS = loginServerStmt.executeQuery("SELECT * FROM gameservers");
			
			while(loginServerRS.next()){
				serverList.id = 8;
				serverList.serverIp = loginServerRS.getString("host");
				serverList.serverPort = loginServerRS.getShort("port");
				serverList.serverAgeLimit = loginServerRS.getShort("age_limit");
				serverList.serverType = loginServerRS.getShort("type");
				serverList.serverOnlineLimit = loginServerRS.getShort("online_limit");
				
				logger.info("ServerList Ok");
			}
		} catch (Exception e) {
			logger.catching(e);
		}
		return serverList;
	}

}
