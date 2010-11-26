package com.persistent.util;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.impl.SessionFactoryImpl;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.resolver.DialectFactory;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("databaseUtil")
@SuppressWarnings("rawtypes")
public class DatabaseUtil {
	
	@Autowired
	protected SessionFactory sessionFactory;
	
	public void printProperties(){
		System.out.println(sessionFactory.toString());
		Properties prop =((SessionFactoryImpl)sessionFactory).getProperties();
		System.out.println(prop.toString());
		System.out.println(prop.getProperty(Environment.DIALECT));
	}
	
	@Transactional
    public void createTables(final Class[] classes) throws SQLException {
		
		// Bypass the interface to the implementation to access the Hibernate config properties
		final Properties properties =((SessionFactoryImpl)sessionFactory).getProperties();
		final Dialect dialect = DialectFactory.buildDialect(properties);
		
		sessionFactory.getCurrentSession().doWork(new Work() {
			public void execute(Connection connection) throws SQLException {
			
				// Create and execute DROP TABLE Statements
				CallableStatement call = connection.prepareCall(generateDropStatements(classes, dialect));
			    call.execute();
			    
			    // Create and execute CREATE TABLE Statements
			    call = connection.prepareCall(generateSchemas(classes, dialect));
			    call.execute();
			}
		});
	}
	
	@Transactional
	public void loadData(final String directory) {
		
		sessionFactory.getCurrentSession().doWork(new Work() {
			public void execute(Connection connection) throws SQLException {
			    try {
			    	// Load data from CSV files located in the specified directory
				    IDataSet dataSet = new CsvDataSet(new File(directory));
					DatabaseOperation.CLEAN_INSERT.execute(new DatabaseConnection(connection), dataSet);
					
				} catch (DatabaseUnitException e) {
					throw new SQLException("Failed to load Data from CSV files in '" + directory + "'" ,e);
				}
			}
		});
	}

    
	public String generateDropStatements(final Class[] classes, final Dialect dialect) {
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
    
    public String generateSchemas(final Class[] classes, final Dialect dialect) {
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

}

