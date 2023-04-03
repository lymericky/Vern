package com.denommeinc.vern.utility;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public enum Unit {
    /**
     * The data does not have a unit (mostly count or ratio)
     */
    NoUnit(""),
    /**
     * The type of the data is unknown
     */
    Unknown(""),
    /**
     * The response contains multiple sub-value, and each one have its own unit
     */
    Multiple(""),
    /**
     * The data is a percentage
     */
    Percent("%"),
    /**
     * The data is a temperature
     */
    DegreeCelsius("°C", 1.8d, 32f, "°F"),
    /**
     * The data is a pressure
     */
    KiloPascal("kPa", 0.14503773800722, 0, "psi"),
    /**
     * The data is a rotational speed
     */
    RoundPerMinute("rpm"),
    /**
     * The data is a speed
     */
    KilometrePerHour("km/h", 0.62137119223733d, 0, "mph"),
    /**
     * The data is an angle
     */
    Degree("°"),
    /**
     * The data is a flow
     */
    GramPerSecond("g/s"),
    /**
     * The dat is a voltage
     */
    Volt("V"),
    /**
     * The data is a duration
     */
    Second("s"),
    /**
     * The data is a distance
     */
    Kilometre("km", 0.62137119223733d, 0, "mi"),
    /**
     * The data is a pressure
     */
    Pascal("Pa", 0.00014503773800722d, 0, "psi"),
    /**
     * The data is a current
     */
    Milliampere("mA"),
    /**
     * The data is a duration
     */
    Minute("min"),
    /**
     * The data is a flow
     */
    LitrePerHour("L/h", 0.26417205124156d, 0, "gal/h"),
    /**
     * The data is a force
     */
    NewtonMetre("Nm", 0.7375621492772656d, 0, "lb-ft");

    private final String symbol;
    private final double imperialCoef;
    private final double imperialOffset;
    private final String imperialSymbol;

    Unit(String symbol, double imperialCoef, double imperialOffset, String imperialSymbol) {
        this.symbol = symbol;
        this.imperialCoef = imperialCoef;
        this.imperialOffset = imperialOffset;
        this.imperialSymbol = imperialSymbol;
    }

    Unit(String symbol) {
        this(symbol, 1f, 0f, symbol);
    }

    /**
     * Convert the metric value into its imperial equivalence
     *
     * @param metricValue The value in the metric unit system
     * @return The same value in the imperial unit system
     */
    public double toImperial(double metricValue) {
        return metricValue * imperialCoef + imperialOffset;
    }

    /**
     * Get the metric unit symbol
     *
     * @return A symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Get the imperial unit symbol (can be the same as the metric)
     *
     * @return A symbol
     */
    public String getImperialSymbol() {
        return imperialSymbol;
    }

    @Override
    public String toString() {
        if (symbol.equals(imperialSymbol)) {
            return symbol;
        }

        DecimalFormat formatter = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        // Display more that 6 digit is pointless
        formatter.setMaximumFractionDigits(6);
        String coefString = formatter.format(imperialCoef);
        String format = "%s (1%s = %s%s)";
        String offsetString = formatter.format(imperialOffset);

        if (imperialOffset > 0) {
            format = "%s (1%s = %s%s " + (imperialOffset > 0 ? "+" : "-") + " %s)";
        }

        return String.format(format, symbol, imperialSymbol, coefString, symbol, offsetString);
    }
}
