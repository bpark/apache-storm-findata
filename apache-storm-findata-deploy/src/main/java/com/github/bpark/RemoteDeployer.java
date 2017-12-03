package com.github.bpark;

import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;

import java.util.Collections;

public class RemoteDeployer {

    public static void main(String[] args) throws Exception {

        Config conf = new Config();
        conf.setDebug(true);

        conf.setNumWorkers(1);
        conf.put(Config.NIMBUS_SEEDS, Collections.singletonList("192.168.77.3"));
        conf.put(Config.NIMBUS_THRIFT_PORT, 6627);

        //NimbusClient nimbusClient = NimbusClient.getConfiguredClient(conf);
        //nimbusClient.getClient().killTopology("numbertop");

        String inputJar = "apache-storm-findata-topology/target/apache-storm-findata-topology-1.0-SNAPSHOT.jar";
        System.setProperty("storm.jar", inputJar);

        StormSubmitter.submitTopologyWithProgressBar("twittertopology", conf, TwitterTopology.createTopology());
    }
}
