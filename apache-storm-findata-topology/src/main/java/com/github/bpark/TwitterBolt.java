package com.github.bpark;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterBolt extends BaseBasicBolt {

    private static final Logger logger = LoggerFactory.getLogger(TwitterBolt.class);

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        logger.info("executing tuple field {}", input.getValueByField("tweet"));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("tweet"));
    }
}
