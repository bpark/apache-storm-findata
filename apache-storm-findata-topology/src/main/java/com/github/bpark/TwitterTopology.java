package com.github.bpark;

import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;

public class TwitterTopology {

    public static StormTopology createTopology() {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("twitterspout", new TwitterSpout(), 1);
        builder.setBolt("twitterbolt", new TwitterBolt(), 1).shuffleGrouping("twitterspout");

        return builder.createTopology();
    }
}
