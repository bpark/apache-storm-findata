package com.github.bpark;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoBolt extends BaseBasicBolt {

    private static final Logger logger = LoggerFactory.getLogger(DemoBolt.class);

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        logger.info("executing tuple field {}", tuple.getIntegerByField("number"));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("number"));
    }

}
