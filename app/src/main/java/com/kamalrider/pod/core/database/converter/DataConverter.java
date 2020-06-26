package com.kamalrider.pod.core.database.converter;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataConverter {
    // region Date to Long
    @TypeConverter
    public static Long fromDate(Date date) {
        if (date == null)
            return (null);

        return (date.getTime());
    }

    @TypeConverter
    public static Date toDate(Long millisSinceEpoch) {
        if (millisSinceEpoch == null)
            return (null);

        return (new Date(millisSinceEpoch));
    }
    //endregion

    // region String List to String
    @TypeConverter
    public String fromStringList(List<String> list) {
        String string = "";
        for (String s : list)
            string += (s + ",");
        return string;
    }

    @TypeConverter
    public List<String> toStringList(String concatenatedStrings) {
        List<String> list = new ArrayList<>();
        for (String s : concatenatedStrings.split(","))
            list.add(s);
        return list;
    }
    //endregion

//    @TypeConverter
//    public static List<String> fromJson(String value) {
//        Type listType = new TypeToken<List<String>>() {
//        }.getType();
//        return new Gson().fromJson(value, listType);
//    }
//
//    @TypeConverter
//    public static String toJson(List<String> list) {
//        Gson gson = new Gson();
//        String json = gson.toJson(list);
//        return json;
//    }
}
