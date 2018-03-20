package com.change.processor;

import com.change.bean.AlipayTranDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * 数据校验
 *
 * @author: feiweiwei
 * @description:
 * @created Date: 14:05 17/11/30.
 * @modify by:
 */
public class AlipayValidateProcessor implements ItemProcessor<AlipayTranDO, AlipayTranDO> {
    private static final Logger log = LoggerFactory.getLogger(AlipayValidateProcessor.class);

    /**
     * 数据校验
     *
     * @param alipayTranDO 数据实体
     * @return 校验结果, 如不符合将直接返回异常
     * @throws Exception 异常处理
     */
    @Override
    public AlipayTranDO process(AlipayTranDO alipayTranDO) throws Exception {
        if (Double.parseDouble(alipayTranDO.getAmount()) < 0) {
            log.info("validate error: " + alipayTranDO.toString());
            throw new Exception();
        } else {
            return alipayTranDO;
        }
    }
}
