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
            PROTOCOL = "AT DP", // Display Used Protocol
            PROTOCOL_1 = "AT_TP1", // 15765
            PROTOCOL_3 = "AT_TP3", // 9141
            PP_14230_KWP4 = "AT TP A4",
            PP_14230_KWP5_FAST = "AT SP A5",
            PROTOCOL_AUTO = "AT_SP0",
            RESET = "AT Z", // "AT Z"
            CLEAR_DTC_CODES = "04",
            PIDS_SUPPORTED = "0100",
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
            PIDS_SUPPORTED20 = "0120", //PIDs supported
            FUEL_TYPE = "0151",
            FUEL_SYSTEM_STATUS = "0103",
            SHOW_DTC_CODES = "03",
            VIN = "0902",
            FUEL_PRESSURE = "010A",
            DISPLAY_SUPPORTED_PP_SUMMARY = "AT PPS",
            SHOW_CAN_STATUS = "AT CS",
            CAN_SILENT_MODE_OFF = "AT CSM0",
            CAN_SILENT_MODE_ON = "AT CSM1",
            AUTOMATIC_RECEIVE = "AT AR",
            ACTIVITY_MONITOR_COUNT = "AT AMC",
            SET_ALL_TO_DEFAULTS = "AT D",
            DISPLAY_DEVICE_DESCRIPTION = "AT @1",
            DISPLAY_DEVICE_IDENTIFIER = "AT @2",
            DISABLE_KEYWORDS = "AT KW0",
            ENABLE_KEYWORDS = "AT KW1",
            MONITOR_ALL_MESSAGES = "AT MA",
            LAST_PROTOCOL_MEMORY_ON = "AT M1",
            LAST_PROTOCOL_MEMORY_OFF = "AT_M0",
            IS_ACTIVE = "AT IA",
            IDENTIFY_YOURSELF = "AT I",
            SET_DEFAULTS = "AT D",
            CHECK_IGNITION_POSITION = "AT IGN";



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
