class VariableContainer {
    private def levelContext
    private def stateMachine

    VariableContainer(levelContext, stateMachine) {
        this.levelContext = levelContext;
        this.stateMachine = stateMachine;            
    }

    def getSelectedPartyMember() {
        return levelContext.getParty().getPartyMember(stateMachine.getSelectedPartyMemberIndex())
    }
         
    def getSelectedCharacterName() {
        return getSelectedPartyMember().getName()
    }   
    
    def getSelectedCharacterGender() {
        return getSelectedPartyMember().getGender().getText()
    }
    
    def getSelectedCharacterSubjectivePronoun() {
        if (getSelectedPartyMember().getGender().getText().equals("Male")) {
            return "he";   
        } else if (getSelectedPartyMember().getGender().getText().equals("Female")) {
            return "she";
        } else {
            return "it";
        }
    }
    
    def getSelectedCharacterPossessivePronoun() {
        if (getSelectedPartyMember().getGender().getText().equals("Male")) {
            return "his"   
        } else if (getSelectedPartyMember().getGender().getText().equals("Female")) {
            return "her"
        } else {
            return "its"
        }
    }
}