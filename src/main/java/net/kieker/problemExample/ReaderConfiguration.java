package net.kieker.problemExample;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import kieker.analysis.stage.DynamicEventDispatcher;
import kieker.analysis.stage.IEventMatcher;
import kieker.analysis.stage.ImplementsEventMatcher;
import kieker.analysis.trace.execution.ExecutionRecordTransformationStage;
import kieker.analysis.trace.reconstruction.TraceReconstructionStage;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.model.repository.SystemModelRepository;
import kieker.tools.source.LogsReaderCompositeStage;
import teetime.framework.Configuration;

public class ReaderConfiguration extends Configuration {

   protected SystemModelRepository systemModelRepositoryNew = new SystemModelRepository();

   public ReaderConfiguration() {
   }
   
   public ProcessingStage getProcessingStage(File kiekerTraceFolder) {
      ProcessingStage processingStage = new ProcessingStage(systemModelRepositoryNew);
      
      TraceReconstructionStage executionStage = createTraceStage(kiekerTraceFolder);
      this.connectPorts(executionStage.getExecutionTraceOutputPort(), processingStage.getInputPort());
      
      return processingStage;
   }

   public TraceReconstructionStage createTraceStage(final File kiekerTraceFolder) {
      final ExecutionRecordTransformationStage executionRecordTransformationStage = createExecutionsStage(kiekerTraceFolder);

      TraceReconstructionStage traceReconstructionStage = new TraceReconstructionStage(systemModelRepositoryNew, TimeUnit.MILLISECONDS, false, Long.MAX_VALUE);
      this.connectPorts(executionRecordTransformationStage.getOutputPort(), traceReconstructionStage.getInputPort());
      return traceReconstructionStage;
   }

   public ExecutionRecordTransformationStage createExecutionsStage(final File kiekerTraceFolder) {
      List<File> inputDirs = new LinkedList<File>();
      inputDirs.add(kiekerTraceFolder);
      LogsReaderCompositeStage logReaderStage = new LogsReaderCompositeStage(inputDirs, true, 4096);

      final ExecutionRecordTransformationStage executionRecordTransformationStage = new ExecutionRecordTransformationStage(systemModelRepositoryNew);

      final DynamicEventDispatcher dispatcher = new DynamicEventDispatcher(null, false, true, false);
      final IEventMatcher<? extends OperationExecutionRecord> operationExecutionRecordMatcher = new ImplementsEventMatcher<>(OperationExecutionRecord.class, null);
      dispatcher.registerOutput(operationExecutionRecordMatcher);

      this.connectPorts(logReaderStage.getOutputPort(), dispatcher.getInputPort());
      this.connectPorts(operationExecutionRecordMatcher.getOutputPort(), executionRecordTransformationStage.getInputPort());
      return executionRecordTransformationStage;
   }
}
