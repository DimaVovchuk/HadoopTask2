package com.lab.epam.runner;


import com.lab.epam.entity.Model;
import com.lab.epam.entity.ResultModel;
import com.lab.epam.hadoop.task2.Combiner;
import com.lab.epam.hadoop.task2.Map;
import com.lab.epam.hadoop.task2.Reduce;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Created by Bohdan-Dmytro_Vovchu on 11/4/2015.
 */
public class Driver extends Configured implements Tool {
    public int run(String[] strings) throws Exception {
        Configuration conf = this.getConf();
        conf.set("mapreduce.output.textoutputformat.separator", ",");
        Job job = Job.getInstance(conf);
        job.setJarByClass(Driver.class);
        Path in = new Path(strings[0]);
        Path out = new Path(strings[1]);
        job.setJobName("IP traffic counter");

        job.setMapperClass(Map.class);
        job.setCombinerClass(Combiner.class);
        job.setReducerClass(Reduce.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Model.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(ResultModel.class);

        TextInputFormat.addInputPath(job, in);

        FileOutputFormat.setOutputPath(job, out);


        boolean success = job.waitForCompletion(true);
//        long typeBRecords = job.getCounters().findCounter("RecordType", "TypeB").getValue();


        return success ? 0 : 1;

    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new Driver(), args);
        System.exit(res);
    }
}
