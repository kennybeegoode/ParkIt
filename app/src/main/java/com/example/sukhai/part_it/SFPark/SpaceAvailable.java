package com.example.sukhai.part_it.SFPark;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Su Khai Koh on 4/20/15.
 */
public class SpaceAvailable {

    // Rate values
    public static final String VALUE_STREET_SWEEP = "Str sweep";
    public static final String VALUE_NO_CHARGE = "No charge";

    // General keys
    private static final String KEY_TYPE = "TYPE";
    private static final String KEY_NAME = "NAME";
    private static final String KEY_DESCRIPTION = "DESC";
    private static final String KEY_TELEPHONE = "TEL";
    private static final String KEY_LOCATION = "LOC";
    private static final String KEY_OPERATION_HOURS = "OPHRS";
    private static final String KEY_RATES = "RATES";

    // Operating hours keys, for off-street parking only
    private static final String KEY_OPERATION_SCHEDULE = "OPS";
    private static final String KEY_FROM = "FROM";
    private static final String KEY_TO = "TO";
    private static final String KEY_BEGIN = "BEG";
    private static final String KEY_END = "END";

    // Hours keys
    private static final String KEY_RATE = "RATE";
    private static final String KEY_RATE_SCHEDULE = "RS";
    private static final String KEY_RATE_QUALIFIER = "RQ";
    private static final String KEY_RATE_RESTRICTION = "RR";

    private boolean onStreet;
    private String name;
    private String description;
    private String phoneNumber;
    private LatLng[] mLatLng;
    private OperationHours[] hours;
    private Rate[] rates;

    public SpaceAvailable(JSONObject space) throws JSONException {

        parseJSONObject(space);
    }

    private void parseJSONObject(JSONObject jsonObject) throws JSONException {

        if (jsonObject == null)
            return;

        String type = jsonObject.getString(KEY_TYPE);
        this.onStreet = type.equals(ParkingInformation.TYPE_ON_STREET);

        String name = jsonObject.getString(KEY_NAME);
        this.name = name == null ? "" : name;

        String desc = jsonObject.getString(KEY_DESCRIPTION);
        this.description = desc == null ? "" : desc;

        JSONObject mRates = jsonObject.getJSONObject(KEY_RATES);
        this.rates = getRates(mRates);

        if (!onStreet) {
            String phone = jsonObject.getString(KEY_TELEPHONE);
            this.phoneNumber = phone == null ? "" : phone;

            String location = jsonObject.getString(KEY_LOCATION);
            mLatLng = getLatLng(location);

            JSONObject operationHours = jsonObject.getJSONObject(KEY_OPERATION_HOURS);
            this.hours = getOperationHours(operationHours);
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LatLng[] getLatLng() {
        return mLatLng;
    }

    public OperationHours[] getOperationHours() {
        return hours;
    }

    public Rate[] getRates() {
        return rates;
    }

    private LatLng[] getLatLng(String location) {

        String[] points = location.split(",");

        LatLng[] latLngs = new LatLng[points.length / 2];

        for (int i = 0, j = 0; i < points.length; i++, j++) {
            float latitude = Float.parseFloat(points[i]);
            float longitude = Float.parseFloat(points[++i]);

            latLngs[j] = new LatLng(latitude, longitude);
        }

        return latLngs;
    }

    private OperationHours[] getOperationHours(JSONObject schedule) throws JSONException {

        if (schedule == null)
            return null;

        JSONArray operationSchedules = schedule.getJSONArray(KEY_OPERATION_SCHEDULE);

        OperationHours[] oHours = new OperationHours[operationSchedules.length()];

        for (int i = 0; i < oHours.length; i++) {
            oHours[i] = new OperationHours(operationSchedules.getJSONObject(i));
        }

        return oHours;
    }

    private Rate[] getRates(JSONObject rates) throws JSONException {

        if (rates == null)
            return null;

        JSONArray rateSchedule = rates.getJSONArray(KEY_RATE_SCHEDULE);

        Rate[] rRates = new Rate[rateSchedule.length()];

        for (int i = 0; i < rRates.length; i++) {
            rRates[i] = new Rate(rateSchedule.getJSONObject(i));
        }

        return rRates;
    }

    public boolean isOnStreet() {
        return onStreet;
    }

    public class OperationHours {

        private String oFromDay;
        private String oToDay;
        private String oBeginTime;
        private String oEndTime;

        public OperationHours() {
            oFromDay = "";
            oToDay = "";
            oBeginTime = "";
            oEndTime = "";
        }

        public OperationHours(JSONObject hours) throws JSONException {

            if (hours == null)
                return;

            oFromDay = hours.getString(KEY_FROM);
            oToDay = hours.getString(KEY_TO);
            oBeginTime = hours.getString(KEY_BEGIN);
            oEndTime = hours.getString(KEY_END);
        }

        public String getDayFrom() {
            return oFromDay;
        }

        public String getDayTo() {
            return oToDay;
        }

        public String getBeginTime() {
            return oBeginTime;
        }

        public String getEndTime() {
            return oEndTime;
        }
    }

    public class Rate {

        private String rBeginTime;
        private String rEndTime;
        private String rRate;
        private String rDescription;
        private String rRateQualifier;
        private String rRateRestriction;

        public Rate(JSONObject rate) throws JSONException {

            if (rate == null)
                return;

            rBeginTime = rate.getString(KEY_BEGIN);
            rEndTime = rate.getString(KEY_END);
            rRate = rate.getString(KEY_RATE);
            rDescription = rate.getString(KEY_DESCRIPTION);
            rRateQualifier = rate.getString(KEY_RATE_QUALIFIER);
            rRateRestriction = rate.getString(KEY_RATE_RESTRICTION);
        }

        public String getBeginTime() {
            return rBeginTime;
        }

        public String getEndTime() {
            return rEndTime;
        }

        public String getRate() {
            return rRate;
        }

        public String getDescription() {
            return rDescription;
        }

        public String getRateQualifier() {
            return rRateQualifier;
        }

        public String getRateRestriction() {
            return rRateRestriction;
        }
    }
}
