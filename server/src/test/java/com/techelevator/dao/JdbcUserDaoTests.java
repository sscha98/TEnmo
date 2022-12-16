package com.techelevator.dao;


import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class JdbcUserDaoTests extends BaseDaoTests{

    private static final Transfer TRANSFER_1 = new Transfer(3001,1,1001,1002,50.00);
    private static final Transfer TRANSFER_2 = new Transfer(3002, 1,1002,1001,100.00);

    private JdbcUserDao sut;
    JdbcTransferDao sutTransfer;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);
        sutTransfer = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void createNewUser() {
        boolean userCreated = sut.create("TEST_USER","test_password");
        Assert.assertTrue(userCreated);
        User user = sut.findByUsername("TEST_USER");
        Assert.assertEquals("TEST_USER", user.getUsername());
    }

    @Test
    public void getBalanceWorks() {

    }

    @Test
    public void testChangesInBalance() {
        boolean transferCreated = sutTransfer.addNewTransfer(TRANSFER_1);
        Assert.assertTrue(transferCreated);

        int id = sut.findIdByUsername("bob");
        Assert.assertEquals(sut.getAccountBalance(id), 950.00, 0.00000009);
    }

}
