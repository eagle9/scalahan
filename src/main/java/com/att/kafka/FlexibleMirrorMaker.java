package com.att.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//transform this to scala? how to use both java and scall together in a single project?

public class FlexibleMirrorMaker {

    private FlexibleMirrorMaker(){}

    public static void main(String[] args) {
        new FlexibleMirrorMaker().run();
    }
    private void run() {
        //System.out.println(args[0] + "," + args[1] + "," + args[2]);
        //System.exit( 0 );

        //TreeMap<String, List<String>> map = getGroups();
        TreeMap<String, List<String>> map = TopicsConf.getGroups();
        for (String group: map.keySet()) {
            System.out.println(group + ":" + map.get(group).toString());
        }

        /*
        for (Map.Entry<String,List<String>> e: map.entrySet()) {
            System.out.println(e.getKey() + ": " + e.getValue().toString());
        }
        System.exit( 0 ); */

        Logger logger = LoggerFactory.getLogger( FlexibleMirrorMaker.class.getName());


        // latch for dealing with multiple threads
        CountDownLatch latch = new CountDownLatch(1);

        // create the consumer runnable
        logger.info("Creating the consumer thread");

        //one thread for one consumer group
        ExecutorService executor = Executors.newFixedThreadPool(map.size());

        int i = 0;
        for (Map.Entry<String,List<String>> e: map.entrySet()) {
            i++;
            //if (i > 5) break;

            String group = e.getKey();
            List<String> topics = e.getValue();
            logger.info("starting a thread for consumer group " + group + " to cover topics: " + topics.toString());

            Thread thread = new Thread(new ConsumerRunnable(group, topics, latch ));
            executor.submit( thread );

            /*
            // add a shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Caught shutdown hook");
                ((ConsumerRunnable) runnable).shutdown();
                try {
                    latch.await();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                logger.info("Application has exited");
            }));
            try {
                latch.await();
            } catch (InterruptedException ex) {
                logger.error("Application got interrupted", ex);
            } finally {
                logger.info("Application is closing");
            }
            */

        }
        executor.shutdown();


    }

    private TreeMap<String, List<String>> getGroups() {
        TreeMap<String,List<String>> map = new TreeMap<>();
        for (int i =0; i < 100; i+=1) {
            //String group = String.format("group%02d",i / 4+1);
            String group = String.format("group%02d2",i +1);

            String topic = String.format("T%02d17", i);
            //testing only two T..17 topics for now
            //if (topic.equals( "TT3317" ) || topic.equals( "T2817" )) {
            List<String> list = map.getOrDefault( group, new LinkedList<>() );
            list.add( topic );
            map.put( group, list );
            //}
        }
        for (int i = 0; i < 10; i++) {
            String group = String.format("u22-%02d2", i);
            List<String> list = new LinkedList<>(  );
            for (int j = 0; j < 10; j++) list.add(String.format("U%d%d22", i, j));
            map.put(group, list);
        }
        return map;
    }

    private KafkaConsumer<String,String> createConsumer(String groupId) {
        KafkaConsumer<String, String> consumer1 = null;
        try {

            Properties properties = new Properties();
            properties.load(new FileInputStream( "conf/consumer.properties" ));

            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            //override group from the .properties file
            properties.setProperty( ConsumerConfig.GROUP_ID_CONFIG, groupId );

            consumer1 = new KafkaConsumer<String, String>( properties );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return consumer1;
    }

    public KafkaProducer<String, String> createKafkaProducer(){
        KafkaProducer<String, String> producer1 = null;
        try {
            Properties properties = new Properties();
            properties.load( new FileInputStream(new File("conf/producer.properties" ) ) );

            properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

            // create the producer
            producer1 = new KafkaProducer<String, String>(properties);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return producer1;

    }
    public class ConsumerRunnable extends Thread {

        private CountDownLatch latch;
        private KafkaConsumer<String, String> consumer;
        private KafkaProducer<String,String> producer;
        private List<String> topics;
        private String groupId;

        private Logger logger = LoggerFactory.getLogger(ConsumerRunnable.class.getName());

        public ConsumerRunnable(String groupId,List<String> topics, CountDownLatch latch) {
            this.latch = latch;
            this.groupId = groupId;
            this.topics = topics;
            /* tried no much help with all T..17 topics
            if (groupId.contains("group29") || groupId.contains( "group34" )) {
                setPriority( Thread.MAX_PRIORITY);
            }*/
            consumer = createConsumer(groupId );
            consumer.subscribe(topics);

            producer = createKafkaProducer();
        }

        @Override
        public void run() {
            // poll for new data
            try {
                while (true) {
                    ConsumerRecords<String, String> records =
                            consumer.poll(Duration.ofMillis(2000)); // new in Kafka 2.0.0
                    if (records.count() > 0) {
                        //logger.info( "received " + records.count() + " messages" );

                        for (ConsumerRecord<String, String> record : records) {
                            //logger.info("Key: " + record.key() + ", Value: " + record.value());
                            //logger.info("Partition: " + record.partition() + ", Offset:" + record.offset());
                            String topic = record.topic();
                            if (topic.startsWith( TopicsConf.PREFIX() + "U")){
                                topic = "U22";
                            }else if (topic.startsWith( TopicsConf.PREFIX() + "T" )){
                                topic = "T17";
                            }
                            producer.send( new ProducerRecord<>( topic, record.key(), record.value() ), new Callback() {
                                @Override
                                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                                    if (e != null) {
                                        logger.error( "Something bad happened", e );
                                    }
                                }
                            } );
                        }
                        producer.flush();
                        //logger.info( "produced " + records.count() + " messages" );

                        //consumer.commitAsync();
                    }
                }
            } catch (WakeupException e) {
                logger.info("Received shutdown signal!");
            } finally {
                consumer.close();
                // tell our main code we're done with the consumer
                latch.countDown();
            }
        }

        public void shutdown() {
            // the wakeup() method is a special method to interrupt consumer.poll()
            // it will throw the exception WakeUpException
            consumer.wakeup();
        }
    }


}
