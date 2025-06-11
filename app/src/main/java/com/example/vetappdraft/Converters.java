package com.example.vetappdraft;

import androidx.room.TypeConverter;

import com.example.vetappdraft.Page.PageType;

import java.util.LinkedList;

public class Converters {

  private static final String FIELD_DELIMITER = "|-|";
  private static final String PAGE_DELIMITER = "$$";

  @TypeConverter
  public String fromPageList(LinkedList<Page> pages) {
    if (pages == null || pages.isEmpty()) return "";

    StringBuilder builder = new StringBuilder();
    for (Page page : pages) {
      builder.append(escape(page.getName())).append(FIELD_DELIMITER)
          .append(page.getType().name()).append(FIELD_DELIMITER)
          .append(escape(page.getContent())).append(FIELD_DELIMITER)
          .append(escape(page.getInstructions())).append(FIELD_DELIMITER)
          .append(page.getAudioResId()).append(FIELD_DELIMITER)
          .append(page.getCallFlag())
          .append(PAGE_DELIMITER);
    }

    // Remove last PAGE_DELIMITER
    return builder.substring(0, builder.length() - PAGE_DELIMITER.length());
  }

  @TypeConverter
  public LinkedList<Page> toPageList(String data) {
    LinkedList<Page> pages = new LinkedList<>();
    if (data == null || data.isEmpty()) return pages;

    String[] pageStrings = data.split(PAGE_DELIMITER);
    for (String pageString : pageStrings) {
      String[] fields = pageString.split(java.util.regex.Pattern.quote(FIELD_DELIMITER));
      if (fields.length == 6) {
        String name = unescape(fields[0]);
        PageType type = PageType.valueOf(fields[1]);
        String content = unescape(fields[2]);
        String instructions = unescape(fields[3]);
        int audioResId = Integer.parseInt(fields[4]);
        boolean callFlag = Boolean.parseBoolean(fields[5]);

        Page page = new Page(name, type, content, instructions, audioResId, callFlag);
        pages.add(page);
      }
    }

    return pages;
  }

  // Escape any characters that could interfere with delimiters
  private String escape(String s) {
    return s.replace(FIELD_DELIMITER, "").replace(PAGE_DELIMITER, "");
  }

  private String unescape(String s) {
    return s; // Placeholder in case you decide to add escaping logic
  }
}
