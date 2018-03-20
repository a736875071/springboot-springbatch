package com.change.controller;

import com.change.config.BatchConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author YangQing
 * @version 1.0.0
 */
@RestController
public class BatchController {
    @Autowired
    private BatchConfig batchConfig;

    @RequestMapping(value = "/batchcontroller", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String startBatch() {
        batchConfig.run();
        return "ok";
    }
}
