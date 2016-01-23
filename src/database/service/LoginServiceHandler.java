package database.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;

import database.pocket.ServerList;

public class LoginServiceHandler implements LoginService.Iface{
	private Statement loginServerStmt;
	private ResultSet loginServerRS;
	public volatile ServerList serverList;
	
	private static final Logger logger = LogManager.getLogger(LoginServiceHandler.class);
	public LoginServiceHandler(Connection loginServerDB) throws SQLException{
		loginServerStmt = loginServerDB.createStatement();
		serverList =  new ServerList();
	}

	public int authorization(String login, String password) throws TException {
		int status = 0;
		try {
			
			loginServerRS = loginServerStmt.executeQuery("SELECT * FROM accounts WHERE login = '"+login+"'AND password = '"+password+"'");
			
			if(loginServerRS.next()){ // true
				
				if(loginServerRS.getInt("accessLevel")!=-1){
				status = 2; // OK
				}else{
					status = -1; //Block
				}
			}else{
				status = 1; // Faild
			}
			}catch (Exception e) {
				logger.catching(e);
		}
		
		return status;
	}

	@Override
	public ServerList serverList(){
		try {
			loginServerRS = loginServerStmt.executeQuery("SELECT * FROM game_servers");
			
			while(loginServerRS.next()){
				serverList.id = loginServerRS.getShort("server_id");
				serverList.serverIp = loginServerRS.getString("host");
				serverList.serverPort = loginServerRS.getShort("port");
				serverList.serverAgeLimit = loginServerRS.getShort("age_limit");
				serverList.serverType = loginServerRS.getShort("type");
				serverList.serverOnlineLimit = loginServerRS.getShort("online_limit");
				
			}
		} catch (Exception e) {
			logger.catching(e);
		}
		return serverList;
	}

}
