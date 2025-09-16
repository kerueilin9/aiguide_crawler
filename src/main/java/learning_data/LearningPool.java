package learning_data;

import usecase.learningPool.learningResult.LearningResult;
import usecase.learningPool.learningTask.LearningTask;
import util.ActionFactory;
import util.HighLevelAction;
import util.LogHelper;

import java.util.*;

public class LearningPool {
    private Queue<LearningTask> learningTasks;
    private Queue<LearningResult> learningResults;
    private Map<String, LearningTask> processingTasks;
    private Boolean stopLearning;

    public LearningPool() {
        this.learningTasks = new LinkedList<>();
        this.learningResults = new LinkedList<>();
        this.processingTasks = new HashMap<>();
        this.stopLearning = false;
    }

    public synchronized void addTask(LearningTask task) {
        boolean succ;
        succ = learningTasks.offer(task);
        assert succ : "add LearningTask fail";
        LogHelper.info("Now learningTask size is: " + learningTasks.size());
    }

    public synchronized void addResult(LearningResult result) {
        if(result.isDone()) {
            processingTasks.remove(result.getTaskID());
            LogHelper.info("Task ID: " + result.getTaskID() + " is done.");
        }
        boolean succ;
        succ = learningResults.offer(result);
        assert succ : "add LearningResult fail";
    }

    public synchronized void addResultByData(List<HighLevelAction> actionSequence, String taskID, int coverageImproved, int learningTargetActionSequenceLength, boolean isDone) {
        if(isDone) {
            processingTasks.remove(taskID);
            LogHelper.info("Task ID: " + taskID + " is done.");
        }
        boolean succ;
        succ = learningResults.offer(new LearningResult(actionSequence, taskID, "", coverageImproved, learningTargetActionSequenceLength, isDone));
        assert succ : "add LearningResult fail";
    }

    public synchronized LearningTask takeTask() {
        LearningTask task = learningTasks.poll();
        if(task != null){
            processingTasks.put(task.getStateID(), task);
            LogHelper.info("Now learningTask size is: " + learningTasks.size());
        }
        return task;
    }

    public synchronized LearningResult takeResult() {
        return learningResults.poll();
    }

    public synchronized List<LearningResult> takeResults() {
        List<LearningResult> results = new ArrayList<>();
        while(!learningResults.isEmpty()){
            results.add(learningResults.poll());
        }
        return results;
    }

    public synchronized void restoreTask(String taskID) {
        LearningTask task = processingTasks.get(taskID);
        if(task == null){
            System.out.println( taskID + " is null.");
            return;
        }
        processingTasks.remove(taskID);
        addTask(task);
        System.out.println( taskID + " has been added in Learning Task Queue.");
    }

    public synchronized int getTaskSize() {
        return learningTasks.size();
    }

    public synchronized int getResultSize() { return learningResults.size(); }

    public synchronized int getProcessingTaskSize() { return processingTasks.size(); }

    public synchronized void setStopLearning() { stopLearning = true; }

    public synchronized Boolean getStopLearning() { return stopLearning; }

    public synchronized ActionFactory getActionFactory() { return new ActionFactory(); }
}
