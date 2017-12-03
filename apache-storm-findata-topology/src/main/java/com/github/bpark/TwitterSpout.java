package com.github.bpark;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bpark.model.Tweet;
import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TwitterSpout extends BaseRichSpout {

    private static final Logger logger = LoggerFactory.getLogger(TwitterSpout.class);

    private static final String CONSUMER_KEY = "consumer_key";
    private static final String CONSUMER_SECRET = "consumer_secret";
    private static final String TOKEN = "token";
    private static final String TOKEN_SECRET = "token_secret";
    private static final String TERMS = "terms";

    private ObjectMapper objectMapper = new ObjectMapper();
    private SpoutOutputCollector collector;
    private Client hosebirdClient;
    private BlockingQueue<String> msgQueue;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        logger.info("initializing");
        this.collector = collector;

        msgQueue = new LinkedBlockingQueue<>(100000);

        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();

        Properties twitterConfig = readConfig();

        //List<Long> followings = Lists.newArrayList(1234L, 566788L);
        List<String> terms = Lists.newArrayList(twitterConfig.getProperty(TERMS).split(","));
        //hosebirdEndpoint.followings(followings);
        hosebirdEndpoint.trackTerms(terms);

        Authentication hosebirdAuth = new OAuth1(
                twitterConfig.getProperty(CONSUMER_KEY),
                twitterConfig.getProperty(CONSUMER_SECRET),
                twitterConfig.getProperty(TOKEN),
                twitterConfig.getProperty(TOKEN_SECRET));

        ClientBuilder builder = new ClientBuilder()
                .name("apache-storm-findata")
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue));

        hosebirdClient = builder.build();
        hosebirdClient.connect();
    }

    @Override
    public void nextTuple() {
        try {
            if (!hosebirdClient.isDone()) {
                String msg = msgQueue.take();
                System.out.println(msg);
                Tweet tweet = objectMapper.readValue(msg, Tweet.class);

                collector.emit(new Values(tweet));
            }
        } catch (Exception e) {
            logger.error("error during tweet emitting!", e);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("tweet"));
    }

    private Properties readConfig() {
        final Properties properties = new Properties();
        try (final InputStream stream = this.getClass().getResourceAsStream("/twitterconfig.properties")) {
            properties.load(stream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
