package usecase.learningPool.action.mapper;

import usecase.learningPool.action.dto.ActionDTO;
import usecase.learningPool.action.dto.HighLevelActionDTO;
import util.Action;
import util.HighLevelAction;

import java.util.ArrayList;
import java.util.List;

public class HighLevelActionDTOMapper {
    public static HighLevelAction mappingHighLevelActionFrom(HighLevelActionDTO highLevelActionDTO){
        List<Action> actionList = new ArrayList<>();
        for(ActionDTO actionDTO: highLevelActionDTO.getActionDTOList()){
            actionList.add(ActionDTOMapper.mappingActionFrom(actionDTO));
        }
        return new HighLevelAction(actionList);
    }
}
