package com.change.processor;


import com.change.bean.AlipayTranDO;
import com.change.bean.HopPayTranDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 数据处理
 *
 * @author: feiweiwei
 * @description:
 * @created Date: 15:19 17/11/27.
 * @modify by:
 */
@Service
public class AlipayItemProcessor implements ItemProcessor<AlipayTranDO, HopPayTranDO> {

    private static final Logger log = LoggerFactory.getLogger(AlipayItemProcessor.class);

    /**
     * 数据处理
     *
     * @param alipayTranDO 数据对象
     * @return 处理后的数据
     * @throws Exception 异常
     */
    @Override
    public HopPayTranDO process(AlipayTranDO alipayTranDO) throws Exception {
        HopPayTranDO hopPayTranDO = new HopPayTranDO();
        hopPayTranDO.setTranId(alipayTranDO.getTranId());
        hopPayTranDO.setChannel(alipayTranDO.getChannel());
        hopPayTranDO.setTranType(alipayTranDO.getTranType());
        hopPayTranDO.setCounterparty(alipayTranDO.getCounterparty());
        hopPayTranDO.setGoods(alipayTranDO.getGoods());
        hopPayTranDO.setAmount(alipayTranDO.getAmount());
        hopPayTranDO.setIsDebitCredit(alipayTranDO.getIsDebitCredit());
        hopPayTranDO.setState(alipayTranDO.getState());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(new Date());
        hopPayTranDO.setTranDate(dateNowStr);
        hopPayTranDO.setMerId("00000001");
        log.info(alipayTranDO.toString());
        return hopPayTranDO;
    }
}
