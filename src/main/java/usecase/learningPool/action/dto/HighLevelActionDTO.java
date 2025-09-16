package usecase.learningPool.action.dto;
import java.util.List;

public class HighLevelActionDTO {
    private final List<ActionDTO> actionDTOList;

    public HighLevelActionDTO(List<ActionDTO> actionDTOList){
        this.actionDTOList = actionDTOList;
    }

    public List<ActionDTO> getActionDTOList() {
        return actionDTOList;
    }

    public void appendActionDTO(ActionDTO actionDTO){
        actionDTOList.add(actionDTO);
    }
}