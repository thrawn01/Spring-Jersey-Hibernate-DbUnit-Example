package com.persistent.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.springframework.stereotype.Service;

@Service("databaseUtil")
public class DatabaseUtil {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public void printProperties(){
		Map<String, Object> result = entityManager.getProperties(); 
		System.out.println(result.toString());
	}
	
    public static void setupTables(final Connection connection, final Class[] classes, final Dialect dialect) throws SQLException {

        try {
            executeSql(connection, generateDropStatements(classes, dialect));
            connection.commit();
        }
        catch (final SQLException se) { 
        }

        try {
            executeSql(connection, generateSchemas(classes, dialect));
        }
        catch (final SQLException se) { }
    }
    
    public static String generateDropStatements(final Class[] classes, final Dialect dialect) {
        final Configuration cfg = new Configuration();
        for(int i=0; i < classes.length; i++) {
            cfg.addAnnotatedClass(classes[i]);
        }
        final String[] lines = cfg.generateDropSchemaScript(dialect);
        
        String createSql = "";
        for (int i = 0; i < lines.length; i++) {
            createSql = createSql + lines[i];
        }
        return createSql;
    }
    
    public static String generateSchemas(final Class[] classes, final Dialect dialect) {
        final Configuration cfg = new Configuration();
        for(int i=0; i < classes.length; i++) {
            cfg.addAnnotatedClass(classes[i]);
        }
        final String[] lines = cfg.generateSchemaCreationScript(dialect);
        
        String createSql = "";
        for (int i = 0; i < lines.length; i++) {
            createSql = createSql + lines[i];
        }
        return createSql;
    }
    
    public static void executeSql(final Connection connection, final String sql) throws SQLException {
        final Statement statement = connection.createStatement();
        statement.execute(sql);
    }

}

