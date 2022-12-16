package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Before;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTransferDaoTests extends BaseDaoTests{

    private static final Transfer TRANSFER_1 = new Transfer(3001,1,1001,1002,50.00);
    private static final Transfer TRANSFER_2 = new Transfer(3002, 1,1002,1001,100.00);

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);
    }
}
