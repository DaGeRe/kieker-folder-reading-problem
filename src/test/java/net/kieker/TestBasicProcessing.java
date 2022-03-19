package net.kieker;

import java.io.File;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import net.kieker.problemExample.ProcessingStage;
import net.kieker.problemExample.ReaderConfiguration;
import teetime.framework.Execution;

public class TestBasicProcessing {
   
   @Test
   public void testBasicProcessing() {
      
      File exampleFile = new File("src/test/resources/kieker-20220319-142141-18720233152963-UTC--KIEKER-KoPeMe");
            
      ReaderConfiguration configuration = new ReaderConfiguration();
      ProcessingStage stage = configuration.getProcessingStage(exampleFile);
      
      Execution execution = new Execution(configuration);
      execution.executeBlocking();
      
      MatcherAssert.assertThat(stage.getSignatures(), Matchers.hasItem("defaultpackage.NormalDependency.child1"));
      
   }
}
