package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserDao implements UserDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int findIdByUsername(String username) {
        String sql = "SELECT user_id FROM tenmo_user WHERE username ILIKE ?;";
        Integer id = jdbcTemplate.queryForObject(sql, Integer.class, username);
        if (id != null) {
            return id;
        } else {
            return -1;
        }
    }

    //when sendMoney is called with a transfer amount and senderId
    //the jdbcTemplate updates the sql table by removing the transfer amount from the current balance where the user_id = senderId
    @Override
    public void sendMoney(double transferAmount, int senderId) {
        String sql = "UPDATE account SET balance = ? WHERE user_id = ?";
        jdbcTemplate.update(sql,getAccountBalance(senderId) -transferAmount,senderId);
    }
    //when sendMoney is called with a transfer amount and receiverId
    //the jdbcTemplate updates the sql table by adding the transfer amount from the current balance where the user_id = receiverId
    @Override
    public void receiveMoney(double transferAmount, int receiverId) {
        String sql = "UPDATE account SET balance = ? WHERE user_id = ?";
        jdbcTemplate.update(sql,getAccountBalance(receiverId) + transferAmount,receiverId);
    }

    //runs a sql query to return all rows of the tenmo_user table
    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user;";
        //returns the rows of the tenmo_user table
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        //while loop for multiple rows in the SqlRowSet
        while(results.next()) {
            //mapRowToUser declares new Users with the column data of the current row being read
            User user = mapRowToUser(results);
            //adds the newly declared user to the users list
            users.add(user);
        }
        return users;
    }


    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user WHERE username ILIKE ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
        if (rowSet.next()){
            return mapRowToUser(rowSet);
        }
        throw new UsernameNotFoundException("User " + username + " was not found.");
    }

    @Override
    public boolean create(String username, String password) {
        //initial balance when a user is registered
        double initialBalance = 1000;
        // create user
        String sql = "INSERT INTO tenmo_user (username, password_hash) VALUES (?, ?) RETURNING user_id";
        //inserts a new row into the account table with the initial balance of 1000 when a new user is registered
        String sqlAccount = "INSERT INTO account (user_id,balance) VALUES(?,?) RETURNING account_id";
        String password_hash = new BCryptPasswordEncoder().encode(password);
        Integer newUserId;
        Integer newAccountId;
        try {
            newUserId = jdbcTemplate.queryForObject(sql, Integer.class, username, password_hash);
            //runs the sql query to add the newly registered user to the account table
            newAccountId = jdbcTemplate.queryForObject(sqlAccount,Integer.class, newUserId,initialBalance);
        } catch (DataAccessException e) {
            return false;
        }

        // TODO: Create the account record with initial balance

        return true;
    }
    //retrieves account balance based on userId
    @Override
    public double getAccountBalance(int userId) {
        //runs sql query to return balance from account table where the user_id matches userId
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        double balance  = jdbcTemplate.queryForObject(sql,Double.class,userId);
        return balance;
    }
    //used to declare new Users based on column data from SqlRowSet
    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setActivated(true);
        user.setAuthorities("USER");
        return user;
    }
}
