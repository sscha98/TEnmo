package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserDao implements UserDao {

    private JdbcTemplate jdbcTemplate;

//    public JdbcUserDao() {
//    }

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

    @Override
    public void sendMoney(double transferAmount, String senderName) {
        String sql = "UPDATE account SET balance = ? WHERE user_id = (SELECT user_id FROM tenmo_user WHERE username = ?)";
        jdbcTemplate.update(sql,getAccountBalance(senderName) -transferAmount,senderName);
    }

    @Override
    public void receiveMoney(double transferAmount, String receiverName) {
        String sql = "UPDATE account SET balance = ? WHERE user_id = (SELECT user_id FROM tenmo_user WHERE username = ?)";
        jdbcTemplate.update(sql,getAccountBalance(receiverName) + transferAmount,receiverName);
    }


    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            User user = mapRowToUser(results);
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
        double initialBalance = 1000;
        // create user
        String sql = "INSERT INTO tenmo_user (username, password_hash) VALUES (?, ?) RETURNING user_id";
        String sqlAccount = "INSERT INTO account (user_id,balance) VALUES(?,?) RETURNING account_id";
        String password_hash = new BCryptPasswordEncoder().encode(password);
        Integer newUserId;
        Integer newAccountId;
        try {
            newUserId = jdbcTemplate.queryForObject(sql, Integer.class, username, password_hash);
            newAccountId=jdbcTemplate.queryForObject(sqlAccount, Integer.class,newUserId,initialBalance);
        } catch (DataAccessException e) {
            return false;
        }

        // TODO: Create the account record with initial balance

        return true;
    }

    @Override
    public double getAccountBalance(String username) {

        String sql = "SELECT balance FROM account WHERE user_id = (SELECT user_id FROM tenmo_user WHERE username = ?)";
        double balance  = jdbcTemplate.queryForObject(sql,Double.class,username);
        return balance;
    }

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
