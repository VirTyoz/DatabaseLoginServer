package database;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import database.service.LoginService;
import database.service.LoginServiceHandler;

public class DatabaseServer {
	private static final Logger logger = LogManager.getLogger(DatabaseServer.class);
	public static void main(String[] args){
		try {
			Connection loginServerDB = DriverManager.getConnection("jdbc:mysql://localhost:3306/LoginServer", "root", "***REMOVED***");
			
			LoginService.Processor<LoginServiceHandler> processor = new LoginService.Processor<LoginServiceHandler>(new LoginServiceHandler(loginServerDB));
			TServerTransport serverTransport = new TServerSocket(6060);
			TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).
                    processor(processor));
			server.serve();
	
		} catch (Exception e) {
			logger.info(e);
		}
	}

}
