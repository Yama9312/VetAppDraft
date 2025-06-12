package com.example.vetappdraft;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Converters {
  @TypeConverter
  public String fromIntegerList(List<Integer> list) {
    if (list == null || list.isEmpty()) return "";
    return list.stream().map(String::valueOf).collect(Collectors.joining(","));
  }

  @TypeConverter
  public List<Integer> toIntegerList(String data) {
    List<Integer> list = new ArrayList<> ();
    if (data == null || data.isEmpty()) return list;
    for (String s : data.split(",")) {
      list.add(Integer.parseInt(s));
    }
    return list;
  }

}
