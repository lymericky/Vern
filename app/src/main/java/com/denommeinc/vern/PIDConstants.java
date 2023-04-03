package com.denommeinc.vern;

public class PIDConstants {

    public PIDConstants() {

    }

    public static final String[] PIDS = {
            "01", "02", "03", "04", "05", "06", "07", "08",
            "09", "0A", "0B", "0C", "0D", "0E", "0F", "10",
            "11", "12", "13", "14", "15", "16", "17", "18",
            "19", "1A", "1B", "1C", "1D", "1E", "1F", "20"
    };
    public static final String
            DIST_TRV_DTC_CLEARED = "0131",
            FUEL_TANK_INPUT = "012F", // %
            ODOMETER = "01A6", // km
            VOLTAGE = "AT RV",
            PROTOCOL = "AT DP",
            RESET = "AT Z", // "AT Z"
            CLEAR_DTC_CODES = "04",
            PIDS_SUPPORTED20 = "0100",
            ENGINE_COOLANT_TEMP = "0105",  //A-40
            ENGINE_RPM = "010C",  //((A*256)+B)/4
            ENGINE_LOAD = "0104",  // A*100/255
            VEHICLE_SPEED = "010D",  //A
            INTAKE_AIR_TEMP = "010F",  //A-40
            MAF_AIR_FLOW = "0110", //MAF air flow rate 0 - 655.35	grams/sec ((256*A)+B) / 100  [g/s]
            ENGINE_OIL_TEMP = "015C",  //A-40
            FUEL_RAIL_PRESSURE = "0122", // ((A*256)+B)*0.079
            INTAKE_MAN_PRESSURE = "010B", //Intake manifold absolute pressure 0 - 255 kPa
            CONT_MODULE_VOLT = "0142",  //((A*256)+B)/1000
            AMBIENT_AIR_TEMP = "0146",  //A-40
            CATALYST_TEMP_B1S1 = "013C",  //(((A*256)+B)/10)-40
            STATUS_DTC = "0101", //Status since DTC Cleared
            THROTTLE_POSITION = "0111", //Throttle position 0 -100 % A*100/255
            OBD_STANDARDS = "011C", //OBD standards this vehicle
            PIDS_SUPPORTED = "0120", //PIDs supported
            FUEL_TYPE = "0151",
            FUEL_SYSTEM_STATUS = "0103",
            SHOW_DTC_CODES = "03",
            VIN = "0902",
            FUEL_PRESSURE = "010A"

    ;


    public static int rpmVal = 0,
            intakeAirTemp = 0,
            ambientAirTemp = 0,
            coolantTemp = 0,
            mMaf = 0,
            engineOilTemp = 0,
            b1s1temp = 0,
            engineType = 0,
            faceColor = 0,
            whichCommand = 0,
            m_detectPids = 0,
            connectCount = 0,
            tryCount = 0;
}
