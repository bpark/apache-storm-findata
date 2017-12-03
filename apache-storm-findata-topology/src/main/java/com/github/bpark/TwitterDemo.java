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
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TwitterDemo {

    private static final String CONSUMER_KEY = "BeeCg0eE2kDLLU967NO42p9PK";
    private static final String CONSUMER_SECRET = "inZKWBymQvZhKBQhf9yhffNvdCOAb1DQkSv7tBlRbujT4SVwXi";
    private static final String TOKEN = "712118503-15Iq3kcTjcM9S5KzBwzDQrbpAm5GzbcOOkZdeBTt";
    private static final String TOKEN_SECRET = "KsYTNe0tOGytgvX316pJ6NzDc02R4b0HKsm90J9d8VRKm";

    public static void main(String[] args) {

        ObjectMapper objectMapper = new ObjectMapper();

        /* Set up your blocking queues: Be sure to size these properly based on expected TPS of your stream */
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);
        BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>(1000);

        /* Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();

        // Optional: set up some followings and track terms
        //List<Long> followings = Lists.newArrayList(1234L, 566788L);
        List<String> terms = Lists.newArrayList("stocks");
        //hosebirdEndpoint.followings(followings);
        hosebirdEndpoint.trackTerms(terms);

        // These secrets should be read from a config file
        Authentication hosebirdAuth = new OAuth1(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);

        ClientBuilder builder = new ClientBuilder()
                .name("apache-storm-findata")                              // optional: mainly for the logs
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue))
                .eventMessageQueue(eventQueue);                          // optional: use this if you want to process client events

        Client hosebirdClient = builder.build();
        // Attempts to establish a connection.
        hosebirdClient.connect();

        AtomicBoolean running = new AtomicBoolean(true);

        Thread thread = new Thread(() -> {
            // on a different thread, or multiple different threads....
            try {
                while (!hosebirdClient.isDone() || running.get()) {
                    String msg = msgQueue.take();
                    System.out.println(msg);
                    Tweet tweet = objectMapper.readValue(msg, Tweet.class);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        running.set(false);

        hosebirdClient.stop();
    }
}
