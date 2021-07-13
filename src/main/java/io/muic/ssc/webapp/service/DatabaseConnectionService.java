package io.muic.ssc.webapp.service;

import com.zaxxer.hikari.HikariDataSource;
import io.muic.ssc.webapp.config.ConfigProperties;
import io.muic.ssc.webapp.config.ConfigurationLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnectionService {

    private final HikariDataSource ds;

    public DatabaseConnectionService() {
        ds = new HikariDataSource();
        ds.setMaximumPoolSize(20);
        ConfigProperties configProperties = ConfigurationLoader.load();
        if(configProperties != null){
            throw new RuntimeException("Unable to read the config.properties");
        }
        ds.setDriverClassName(configProperties.getDriverClassName());
        ds.setJdbcUrl(configProperties.getConnectionUrl());
        ds.addDataSourceProperty("user",configProperties.getUsername());
        ds.addDataSourceProperty("password",configProperties.getPassword());
        ds.setAutoCommit(false);
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    /*
    public static void main(String[] args) {
        final HikariDataSource ds = new HikariDataSource();
        ds.setMaximumPoolSize(20);
        ds.setDriverClassName("com.mariadb.cj.jdbc.Driver");
        ds.setJdbcUrl("jdbc:mysql://localhost:13306/login_webapp");
        ds.addDataSourceProperty("user","root");
        ds.addDataSourceProperty("password","password");
        ds.setAutoCommit(false);

        try{
            Connection connection = ds.getConnection();
            String sql = "INSERT INTO user (user_name, password, display_name) VALUES (?,?,?);";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,"my_username");
            ps.setString(2,"my_password");
            ps.setString(3,"my_display_name");
            ps.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     */

}
