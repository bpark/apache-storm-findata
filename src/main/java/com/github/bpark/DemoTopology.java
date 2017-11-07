/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.bpark;


import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

import java.util.Collections;

/**
 * This is a basic example of a Storm topology.
 */
public class DemoTopology {

    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("numberspout", new DemoSpout(), 1);
        builder.setBolt("demobolt", new DemoBolt(), 1).shuffleGrouping("numberspout");

        Config conf = new Config();
        conf.setDebug(true);

        conf.setNumWorkers(1);
        //conf.put(Config.NIMBUS_SEEDS, Collections.singletonList("192.168.77.3"));
        //conf.put(Config.NIMBUS_THRIFT_PORT, 6627);

        //String inputJar = "target/apache-storm-findata-1.0-SNAPSHOT.jar";
        //System.setProperty("storm.jar", inputJar);

        //StormSubmitter.submitTopologyWithProgressBar("numbertop", conf, builder.createTopology());

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test", conf, builder.createTopology());
        Utils.sleep(10000);
        cluster.killTopology("test");
        cluster.shutdown();
    }
}
