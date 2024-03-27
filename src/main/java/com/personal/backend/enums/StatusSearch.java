package com.personal.backend.enums;

import com.google.gson.annotations.SerializedName;

/***
 * Enum to represent the status of the search. I will sugest to add a new status called "ERROR" to represent when the search has an error.
 */
public enum StatusSearch {
    @SerializedName("active")
    ACTIVE,
    @SerializedName("done")
    DONE;

}
