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
    public void testBalanceGetter(){


        double expected = 1000;
        double actual =  sut.getAccountBalance(1002);
        Assert.assertEquals(expected,actual,.00009);
    }
    @Test
    public void testChangesInBalance() {
        sut.create("TEST_USER1","test_password");
        User user1 = sut.findByUsername("TEST_USER1");
        sut.create("TEST_USER2","test_password");
        User user2 = sut.findByUsername("TEST_USER2");

        Transfer transfer = new Transfer(3001,1, user1.getId(), user2.getId(),50.00);
        boolean transferCreated = sutTransfer.addNewTransfer(transfer);
        Assert.assertTrue(transferCreated);

        double senderExpectedBalance = 950.00;
        double receiverExpectedBalance = 1050.00;

        double actualSenderBalance = sut.getAccountBalance(user1.getId());
        double actualReceiverBalance = sut.getAccountBalance(user2.getId());

        //int id = sut.findIdByUsername("bob");
        Assert.assertEquals(senderExpectedBalance, actualSenderBalance, 0.00000009);
        Assert.assertEquals(receiverExpectedBalance, actualReceiverBalance, 0.00000009);
    }

}
