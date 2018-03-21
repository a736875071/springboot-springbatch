package com.change.writer;

import com.change.bean.HopPayTranDO;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 写数据方式,将数据写进数据库
 *
 * @author: feiweiwei
 * @description:
 * @created Date: 14:28 17/11/29.
 * @modify by:
 */
@Service
public class AlipayDBChangeItemWriter implements ItemWriter<HopPayTranDO> {
    private static final String INSERT_ALYPAY_TRAN =
            "insert into alipay_tran_today_copy(tran_id, channel, tran_type, counter_party, goods, amount, is_debit_credit, state,tran_date,mer_id) values(?,?,?,?,?,?,?,?,?,?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 将数据写入数据库
     *
     * @param list 集合
     * @throws Exception 异常信息
     */
    @Override
    public void write(List<? extends HopPayTranDO> list) throws Exception {
        for (HopPayTranDO alipayTran : list) {
            jdbcTemplate.update(INSERT_ALYPAY_TRAN,
                    alipayTran.getTranId(),
                    alipayTran.getChannel(),
                    alipayTran.getTranType(),
                    alipayTran.getCounterparty(),
                    alipayTran.getGoods(),
                    alipayTran.getAmount(),
                    alipayTran.getIsDebitCredit(),
                    alipayTran.getState(),
                    alipayTran.getTranDate(),
                    alipayTran.getMerId());
        }
    }
}
