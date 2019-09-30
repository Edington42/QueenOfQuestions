package com.example.nicholas.queenofquestions;

/***
 * Enumeration to represent the available buttons.
 */
public enum Button {
    TOP,TOP_MID,BOTTOM_MID,BOTTOM, UNKNOWN;

    /**
     * Gets the resource ID of this button
     * @return int resource ID for this button.
     */
    public int getId(){
        switch (this){
            case TOP:
                return R.id.top_button;
            case TOP_MID:
                return R.id.top_mid_button;
            case BOTTOM_MID:
                return R.id.bottom_mid_button;
            case BOTTOM:
                return R.id.bottom_button;
            default:
                //Not sure what really should happen here, probably should break.
                return -1;
        }
    }

    /**
     * Gets a button from the id
     * @param id Id of the button you want
     * @return Button that matches the ID
     */
    static public Button fromId(int id){
        for(Button button:Button.values()){
            if(id == button.getId()) return button;
        }
        //if none are found, return unknown
        return UNKNOWN;
    }
}
