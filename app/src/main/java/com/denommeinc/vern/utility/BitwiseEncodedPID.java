package com.denommeinc.vern.utility;

import java.util.regex.Pattern;

public class BitwiseEncodedPID {

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String vin = " ";
    String workingData;
    String result;
    String buffer;
    String rawData;

    public BitwiseEncodedPID(String buffer) {
        this.buffer = buffer;
        performCalculations(buffer);
        decodeBuffer(buffer);
    }

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s");
    private static final Pattern BUSINIT_PATTERN = Pattern.compile("(BUS INIT)|(BUSINIT)|(\\.)");
    private static final Pattern SEARCHING_PATTERN = Pattern.compile("SEARCHING");
    private static final Pattern DIGITS_LETTERS_PATTERN = Pattern.compile("([0-9A-F])+");

    protected String replaceAll(Pattern pattern, String input, String replacement) {
        return pattern.matcher(input).replaceAll(replacement);
    }

    protected String removeAll(Pattern pattern, String input) {
        return pattern.matcher(input).replaceAll("");
    }

    protected void performCalculations(String buffer) {

    }

    public void decodeBuffer(String buffer) {

    }

    public String convertToString(String hex) {
        StringBuilder sb = new StringBuilder();


        return sb.toString();
    }
}


