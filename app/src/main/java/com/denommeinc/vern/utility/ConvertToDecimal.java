package com.denommeinc.vern.utility;

public class ConvertToDecimal {
    String hexValue = " ";
    int len = hexValue.length();
    int base = 1;
    int decVal = 0;

    public ConvertToDecimal(String hexValue) {
        this.hexValue = hexValue;

    }

    public ConvertToDecimal() {
    }

    public int getDecVal(String hexValue) {
        for (int i = len - 1; i >= 0; i--) {
            if (hexValue.charAt(i) >= '0' && hexValue.charAt(i) <= '9') {
                decVal += (hexValue.charAt(i) - 48) * base;
                base = base * 16;
            }
            else if (hexValue.charAt(i) >= 'A' && hexValue.charAt(i) <= 'F') {
                decVal += (hexValue.charAt(i) - 55) * base;
                base = base * 16;
            }
        }
        return decVal;
    }
}
