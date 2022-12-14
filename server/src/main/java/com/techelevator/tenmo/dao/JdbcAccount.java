package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpHeaders;
import java.security.Principal;

@Component
public class JdbcAccount implements AccountDao {
    private UserDao userDao;
    JdbcTemplate jdbcTemplate;


    public JdbcAccount(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Account findAccountByUsername(UserDao userDao) {


        return null;
    }

    @Override
    public double getAccountBalance(int userId) {
        Account account = null;
        String sql = "SELECT * FROM account WHERE user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql,userId);

        // put try catch/ exception handling
        if (result.next()){
            account = mapRowToAccount(result);
        }
        return account.getBalance();
    }

    @Override
    public int findAccountIdByUsername(String username) {
        return 0;
    }

    private Account mapRowToAccount(SqlRowSet rs){
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getDouble("balance"));
        return account;
    }
}
