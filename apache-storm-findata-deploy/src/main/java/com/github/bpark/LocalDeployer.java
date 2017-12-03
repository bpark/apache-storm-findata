package com.github.bpark;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.utils.Utils;

public class LocalDeployer {

    public static void main(String[] args) {

        Config conf = new Config();
        conf.setDebug(true);

        conf.setNumWorkers(1);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("twitter", conf, TwitterTopology.createTopology());
        Utils.sleep(10000);
        cluster.killTopology("twitter");
        cluster.shutdown();
    }
}
