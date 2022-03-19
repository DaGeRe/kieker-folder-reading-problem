package net.kieker.problemExample;

import java.util.LinkedList;
import java.util.List;

import kieker.analysis.trace.AbstractTraceProcessingStage;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.Execution;
import kieker.model.system.model.ExecutionTrace;

public class ProcessingStage extends AbstractTraceProcessingStage<ExecutionTrace> {

   private final List<String> signatures = new LinkedList<>();
   
   public ProcessingStage(final SystemModelRepository systemModelRepository) {
      super(systemModelRepository);

   }

   @Override
   protected void execute(final ExecutionTrace trace) throws Exception {
      for (final Execution execution : trace.getTraceAsSortedExecutionSet()) {
         final String fullClassname = execution.getOperation().getComponentType().getFullQualifiedName();
         final String methodname = execution.getOperation().getSignature().getName();
         final String signature = fullClassname + "." + methodname;
         System.out.println(signature);
         signatures.add(signature);
      }
   }

   public List<String> getSignatures() {
      return signatures;
   }
}

