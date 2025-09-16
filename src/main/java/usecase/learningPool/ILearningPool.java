package usecase.learningPool;

import usecase.learningPool.learningResult.dto.LearningResultDTO;
import usecase.learningPool.learningTask.dto.LearningTaskDTO;

public interface ILearningPool {
    void startLearningPool();
    void stopLearningPool();
    boolean getAgentDone();
    void setAgentDone(boolean isAgentDone);
    void  enQueueLearningTaskDTO(LearningTaskDTO learningTaskDTO);
    boolean isLearningTaskDTOQueueEmpty();
    LearningResultDTO deQueueLearningResultDTO();
    boolean isLearningResultDTOQueueEmpty();
    boolean getPauseAgent();
    void setPauseAgent(boolean isPauseAgent);
}
