package com.gausslab.managerapp.addnewworksiteform;

public class AddNewWorksiteFormState {
    private final String worksiteNameErrorMessage;
    private final String startDateErrorMessage;
    private final String lastDateErrorMessage;
    private final String locationErrorMessage;
    private final boolean isFieldsValid;


    public AddNewWorksiteFormState(String worksiteNameErrorMessage, String startDateErrorMessage, String lastDateErrorMessage, String locationErrorMessage, boolean isFieldsValid) {
        this.worksiteNameErrorMessage = worksiteNameErrorMessage;
        this.startDateErrorMessage = startDateErrorMessage;
        this.lastDateErrorMessage = lastDateErrorMessage;
        this.locationErrorMessage = locationErrorMessage;
        this.isFieldsValid = isFieldsValid;
    }

    public String getWorksiteNameErrorMessage() {
        return worksiteNameErrorMessage;
    }

    public String getStartDateErrorMessage() {
        return startDateErrorMessage;
    }

    public String getLastDateErrorMessage() {
        return lastDateErrorMessage;
    }

    public String getLocationErrorMessage() {
        return locationErrorMessage;
    }

    public boolean isFieldsValid() {
        return isFieldsValid;
    }
}
