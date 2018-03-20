package com.change.writer;

import com.change.bean.HopPayTranDO;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

/**
 * 将数据写入文本文件
 *
 * @author: feiweiwei
 * @description:
 * @created Date: 14:46 17/11/28.
 * @modify by:
 */
@Service
public class AlipayFileItemWriter {

    /**
     * 将数据写入文本文件
     *
     * @return 写出数据
     */
    public FlatFileItemWriter<HopPayTranDO> getAlipayItemWriter() {
        FlatFileItemWriter<HopPayTranDO> txtItemWriter = new FlatFileItemWriter<HopPayTranDO>();
        txtItemWriter.setAppendAllowed(true);
        txtItemWriter.setShouldDeleteIfExists(true);
        txtItemWriter.setEncoding("UTF-8");
        Long date = System.currentTimeMillis();
        txtItemWriter.setResource(new FileSystemResource("data/" + date + ".txt"));
        txtItemWriter.setLineAggregator(new DelimitedLineAggregator<HopPayTranDO>() {{
            setDelimiter(",");
            setFieldExtractor(new BeanWrapperFieldExtractor<HopPayTranDO>() {{
                setNames(new String[]{"tranId", "channel", "tranType", "counterparty", "goods", "amount", "isDebitCredit", "state", "tranDate", "merId"});
            }});
        }});
        return txtItemWriter;
    }
}
