package usecase.learningPool.action.dto;

import java.util.ArrayList;
import java.util.List;

public class HighLevelActionDTOBuilder {

    private List<ActionDTO> actionDTOList;

    public HighLevelActionDTOBuilder(){}

    public List<ActionDTO> getActionDTOList() {
        return actionDTOList;
    }

    public void setActionDTOList(List<ActionDTO> actionDTOList) {
        this.actionDTOList = actionDTOList;
    }

    public void setActionDTOList(){
        this.actionDTOList = new ArrayList<>();
    }

    public void appendActionDTO(ActionDTO actionDTO){
        this.actionDTOList.add(actionDTO);
    }

    public void appendActionDTO(String xpah, String value){
        this.actionDTOList.add(new ActionDTO(xpah, value));
    }

    public HighLevelActionDTO build(){
        return new HighLevelActionDTO(actionDTOList);
    }
}
