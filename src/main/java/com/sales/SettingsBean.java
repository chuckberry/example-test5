package com.sales;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SettingsBean {

    @JsonProperty("exempt")
    public List<String> exempt = null;
    @JsonProperty("standardTax")
    public double standardTax;
    @JsonProperty("importedTax")
    public double importedTax;

    /**
     * No args constructor for use in serialization
     *
     */
    public SettingsBean() {
    }


    public List<String> getExempt() {
        return exempt;
    }

    public double getStandardTax() {
        return standardTax;
    }

    public double getImportedTax() {
        return importedTax;
    }

}
