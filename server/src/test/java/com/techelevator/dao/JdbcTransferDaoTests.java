package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDaoTests extends BaseDaoTests{

    private static final Transfer TRANSFER_1 = new Transfer(3001,1,1001,1002,50.00);
    private static final Transfer TRANSFER_2 = new Transfer(3002, 1,1002,1001,100.00);

    JdbcUserDao sutUser;
    JdbcTransferDao sutTransfer;
    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sutUser = new JdbcUserDao(jdbcTemplate);
        sutTransfer = new JdbcTransferDao(jdbcTemplate);


    }

    @Test
    public void getTransferList(){
        List<Transfer> testTransferList = new ArrayList<>();
        testTransferList.add(TRANSFER_1);
        testTransferList.add(TRANSFER_2);
        Assert.assertTrue(transferComparison(testTransferList,sutTransfer.listByUserId(1002)));


    }
//    @Test
//    public void getTransfer

    public boolean transferComparison(List<Transfer> expected, List<Transfer> actual){

        if (expected.size()!=actual.size()){
            return false;
        }
        for (int i = 0; i<expected.size(); i++){
            if (expected.get(i).getId()==actual.get(i).getId() && expected.get(i).getTransferTypeId() == actual.get(i).getTransferTypeId()
                && expected.get(i).getSenderId()==actual.get(i).getSenderId() && expected.get(i).getReceiverId()==actual.get(i).getReceiverId()
                && expected.get(i).getTransferAmount()==actual.get(i).getTransferAmount()){

            } else{
                return false;
            }
        }
        return true;


    }
}
