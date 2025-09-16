package usecase.learningPool.action.mapper;

import usecase.learningPool.action.dto.ActionDTO;
import util.Action;

public class ActionDTOMapper {
    public static ActionDTO mappingActionDTOFrom(Action action){
        return new ActionDTO(action.getXpath(), action.getValue());
    }

    public static Action mappingActionFrom(ActionDTO actionDTO){
        return new Action(actionDTO.getXpath(), actionDTO.getValue());
    }
}
