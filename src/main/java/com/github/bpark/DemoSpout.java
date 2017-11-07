package com.github.bpark;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;

public class DemoSpout extends BaseRichSpout {

    private static final Logger logger = LoggerFactory.getLogger(DemoSpout.class);

    private SpoutOutputCollector collector;
    private Random random;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        logger.info("initializing");
        System.out.println("initializing");
        this.collector = collector;
        this.random = new Random();
    }

    @Override
    public void nextTuple() {
        int number = random.nextInt(100);

        logger.info("emitting number {}", number);

        collector.emit(new Values(number));
        Utils.sleep(100);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("number"));
    }
}
