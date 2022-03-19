package net.kieker.problemExample;

import java.io.File;

import teetime.framework.Execution;

public class ReadingStarter {
   public static void main(String[] args) {
      ReaderConfiguration configuration = new ReaderConfiguration();
      ProcessingStage stage = configuration.getProcessingStage(new File(args[0]));
      
      Execution execution = new Execution(configuration);
      execution.executeBlocking();
   }
}
