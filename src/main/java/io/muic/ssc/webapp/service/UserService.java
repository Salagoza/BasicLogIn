package io.muic.ssc.webapp.service;

import io.muic.ssc.webapp.model.User;
import lombok.Data;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private static final String INSERT_USER_SQL = "INSERT INTO user (username,password,display_name) VALUES (?,?,?);";
    private static final String SELECT_USER_SQL = "SELECT * FROM user WHERE username = ?;";
    private static final String SELECT_ALL_USERS_SQL = "SElECT * FROM user;";
    private static final String DELETE_ALL_USERS_SQL = "DELETE FROM user WHERE username = ?;";
    private static final String UPDATE_USER_SQL = "UPDATE user SET display_name = ? WHERE username = ?;";

    private static UserService service;
    private DatabaseConnectionService databaseConnectionService;

    private UserService(){

    }

    public static UserService getInstance(){
        if (service == null){
            service = new UserService();
            service.setDatabaseConnectionService(DatabaseConnectionService.getInstance());
        }
        return service;
    }
    private void setDatabaseConnectionService(DatabaseConnectionService databaseConnectionService){
        this.databaseConnectionService = databaseConnectionService;
    }

    //create new user
    public void createUser(String username, String password ,String displayName) throws UserServiceException{
        try{
            Connection connection = databaseConnectionService.getConnection();
            PreparedStatement ps = connection.prepareStatement(INSERT_USER_SQL);
            ps.setString(1,username);
            ps.setString(2,BCrypt.hashpw(password,BCrypt.gensalt()));
            ps.setString(3,displayName);
            ps.executeUpdate();
            connection.commit();

        }
        catch (SQLIntegrityConstraintViolationException e){
            throw new UsernameNotUniqueException(String.format("Username: %s has already been taken.",username));
        }
        catch (SQLException throwables) {
            throw new UserServiceException(throwables.getMessage());
        }
    }

    // find user by username
    public User findbyUsername(String username){
        try(
                Connection connection = databaseConnectionService.getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_USER_SQL);

        ){
            ps.setString(1,username);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return new User(
                    resultSet.getLong("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("display_Name")
            );

        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
           return null;
        }
    }

    // list all users in the database
    public List<User> findAll(){
        List<User> users = new ArrayList<>();
        try(
                Connection connection = databaseConnectionService.getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_ALL_USERS_SQL);
        ){

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                users.add(new User(
                        resultSet.getLong("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("display_Name")
                ));
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }

    // delete user
    public boolean deleteUserByUsername(String username){
        try(
                Connection connection = databaseConnectionService.getConnection();
                PreparedStatement ps = connection.prepareStatement(DELETE_ALL_USERS_SQL);
        ){
            ps.setString(1,username);
            int deleteCount = ps.executeUpdate();
            connection.commit();
            return deleteCount>0;
        } catch (SQLException throwables) {
            return false;
        }
    }

    public void updateUserByUsername(String username, String displayName) throws UserServiceException {
        try{
            Connection connection = databaseConnectionService.getConnection();
            PreparedStatement ps = connection.prepareStatement(UPDATE_USER_SQL);
            ps.setString(1,username);
            ps.setString(2,displayName);
            ps.executeUpdate();

            connection.commit();
        }
        catch (SQLException throwables) {
            throw new UserServiceException(throwables.getMessage());
        }
    }

    public static void main(String[] args) throws UserServiceException{
        UserService userService = UserService.getInstance();
        try{
            userService.createUser("copter","cop1234567","Salagoza");
        } catch (UserServiceException e) {
            e.printStackTrace();
        }
    }


}
