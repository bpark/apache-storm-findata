# apache-storm-findata
demo project to demonstrate financial data analysis with apache storm.

## Installation

1. Download vagrant Version **1.9.5** or greater from

   https://www.vagrantup.com/downloads.html
   
2. Download VirtualBox Version **5.1.22** or greater from

   https://www.virtualbox.org/wiki/Downloads
   	
3. Go to the directory **vagrant** and type
   	
   ```
   vagrant up	
   ```
4. Login to the virtual machine
   ```
   vagrant ssh	
   ```
   
The virtual machine runs with `192.168.77.3`.

To build a local cluster environment type:

   ```
   mvn clean install	
   ```
   
To build and run the topology on a remote cluster just type:
   ```
   mvn clean install -Premote	
   ```   
   
and start

   ```
   java -jar apache-storm-findata-deploy/target/apache-storm-findata-deploy-1.0-SNAPSHOT-fat.jar	
   ```
   
    
    
    
   