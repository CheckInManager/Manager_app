package com.gausslab.managerapp.addnewworksiteform;

public class AddNewWorksiteFormState {
    private final String worksiteNameErrorMessage;
    private final String startDateErrorMessage;
    private final String endDateErrorMessage;
    private final String locationErrorMessage;
    private final boolean isFieldsValid;


    public AddNewWorksiteFormState(String worksiteNameErrorMessage, String startDateErrorMessage, String endDateErrorMessage, String locationErrorMessage, boolean isFieldsValid) {
        this.worksiteNameErrorMessage = worksiteNameErrorMessage;
        this.startDateErrorMessage = startDateErrorMessage;
        this.endDateErrorMessage = endDateErrorMessage;
        this.locationErrorMessage = locationErrorMessage;
        this.isFieldsValid = isFieldsValid;
    }

    public String getWorksiteNameErrorMessage() {
        return worksiteNameErrorMessage;
    }

    public String getStartDateErrorMessage() {
        return startDateErrorMessage;
    }

    public String getEndDateErrorMessage() {
        return endDateErrorMessage;
    }

    public String getLocationErrorMessage() {
        return locationErrorMessage;
    }

    public boolean isFieldsValid() {
        return isFieldsValid;
    }
}
