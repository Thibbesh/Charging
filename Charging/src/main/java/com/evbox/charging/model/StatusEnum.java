package com.evbox.charging.model;

/**
 * status of charging sessions
 */
public enum StatusEnum {

    IN_PROGRESS("In Progress"),
    FINISHED("Finished");

    private final String statusName;

    StatusEnum(String statusName) {
        this.statusName = statusName;
    }

    /**
     *
     * @return statusName
     */
    @Override
    public String toString() {
        return this.statusName;
    }
}
