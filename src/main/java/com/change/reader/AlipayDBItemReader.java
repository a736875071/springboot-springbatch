package com.change.reader;

import com.change.bean.AlipayTranDO;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 读取数据方式
 *
 * @author: feiweiwei
 * @description:
 * @created Date: 14:23 17/11/28.
 * @modify by:
 */
@Service
public class AlipayDBItemReader {
    @Autowired
    private DataSource dataSource;

    /**
     * 读取数据库
     *
     * @return 将读取的数据转换成对象
     */
    public JdbcCursorItemReader<AlipayTranDO> getAlipayDBItemReader() {
        JdbcCursorItemReader<AlipayTranDO> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("select tran_id,channel,tran_type,counter_party,goods,amount,is_debit_credit,state from alipay_tran_today limit 100");
        reader.setRowMapper(new RowMapper<AlipayTranDO>() {
            @Override
            public AlipayTranDO mapRow(ResultSet resultSet, int i) throws SQLException {
                AlipayTranDO alipayTranDO = new AlipayTranDO();
                alipayTranDO.setTranId(resultSet.getString("tran_id"));
                alipayTranDO.setChannel(resultSet.getString("channel"));
                alipayTranDO.setTranType(resultSet.getString("tran_type"));
                alipayTranDO.setCounterparty(resultSet.getString("counter_party"));
                alipayTranDO.setGoods(resultSet.getString("goods"));
                alipayTranDO.setAmount(resultSet.getString("amount"));
                alipayTranDO.setIsDebitCredit(resultSet.getString("is_debit_credit"));
                alipayTranDO.setState(resultSet.getString("state"));
                return alipayTranDO;
            }
        });
        return reader;
    }
}
