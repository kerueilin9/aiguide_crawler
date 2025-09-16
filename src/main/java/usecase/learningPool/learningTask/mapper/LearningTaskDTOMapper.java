package usecase.learningPool.learningTask.mapper;

import usecase.learningPool.action.dto.ActionDTO;
import usecase.learningPool.action.dto.HighLevelActionDTO;
import usecase.learningPool.action.mapper.ActionDTOMapper;
import usecase.learningPool.learningTask.LearningTask;
import usecase.learningPool.learningTask.dto.LearningTaskDTO;
import util.Action;

import java.util.ArrayList;
import java.util.List;

public class LearningTaskDTOMapper {
    public static LearningTaskDTO mappingLearningTaskDTOFrom(LearningTask learningTask){
        List<HighLevelActionDTO> highLevelActionDTOList = new ArrayList<>();
        for (List<Action> actionList: learningTask.getActionSequence()){
            List<ActionDTO> actionDTOList = new ArrayList<>();
            for (Action action: actionList){
                actionDTOList.add(ActionDTOMapper.mappingActionDTOFrom(action));
            }
            highLevelActionDTOList.add(new HighLevelActionDTO(actionDTOList));
        }

        int codeCoverageVectorLength = learningTask.getCoverage().length;
        boolean[] codeCoverageVector = new boolean[codeCoverageVectorLength];
        for (int i=0; i < codeCoverageVectorLength; i++){
            codeCoverageVector[i] = !(learningTask.getCoverage()[i] == 0);
        }
        return new LearningTaskDTO(highLevelActionDTOList, codeCoverageVector, learningTask.getTargetURL(),
                learningTask.getStateID(), learningTask.getFormXPaths(), learningTask.getLearningConfig());
    }
}